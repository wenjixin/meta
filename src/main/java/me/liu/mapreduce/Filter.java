package me.liu.mapreduce;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by tongwei on 17/8/16.
 */
public class Filter {

    static class MetricTuple implements Writable {

        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        long sum = 0;

        @Override
        public String toString() {
            return "MetricTuple{" +
                    "min=" + min +
                    ", max=" + max +
                    ", sum=" + sum +
                    '}';
        }

        public void reset() {
            min = Long.MAX_VALUE;
            max = Long.MIN_VALUE;
            sum = 0;
        }

        public void accept(long value) {

            if (min > value) {
                min = value;
            }
            if (max < value) {
                max = value;
            }
            sum = sum + value;
        }

        public void accept(MetricTuple value) {

            if (min > value.min) {
                min = value.min;
            }
            if (max < value.max) {
                max = value.max;
            }
            sum = sum + value.sum;
        }

        @Override
        public void write(DataOutput out) throws IOException {
            out.writeLong(min);
            out.writeLong(max);
            out.writeLong(sum);
        }

        @Override
        public void readFields(DataInput in) throws IOException {
            min = in.readLong();
            max = in.readLong();
            sum = in.readLong();
        }
    }


    public static class MapSide extends Mapper<LongWritable, Text, LongWritable, MetricTuple> {


        private LongWritable outKey = new LongWritable(1);

        private MetricTuple metricTuple = new MetricTuple();

        /**
         * Called once at the beginning of the task.
         */
        protected void setup(Context context
        ) throws IOException, InterruptedException {
            // NOTHING
        }

        /**
         * Called once for each key/value pair in the input split. Most applications
         * should override this, but the default is the identity function.
         */
        @SuppressWarnings("unchecked")
        protected void map(LongWritable key, Text value,
                           Context context) throws IOException, InterruptedException {
            outKey.set(key.get() % 2);
            metricTuple.reset();
            metricTuple.accept(Long.valueOf(value.toString()));
            context.write(outKey, metricTuple);
        }

        /**
         * Called once at the end of the task.
         */
        protected void cleanup(Context context
        ) throws IOException, InterruptedException {


        }

    }

    public static class ReduceSide extends Reducer<LongWritable, MetricTuple, LongWritable, MetricTuple> {


        private MetricTuple metricTuple = new MetricTuple();

        /**
         * Called once at the start of the task.
         */
        protected void setup(Context context
        ) throws IOException, InterruptedException {
        }

        /**
         * This method is called once for each key. Most applications will define
         * their reduce class by overriding this method. The default implementation
         * is an identity function.
         */
        @SuppressWarnings("unchecked")
        protected void reduce(LongWritable key, Iterable<MetricTuple> values, Context context
        ) throws IOException, InterruptedException {

            metricTuple.reset();
            for (MetricTuple value :
                    values) {
                metricTuple.accept(value);
            }
            context.write(key, metricTuple);
        }

        /**
         * Called once at the end of the task.
         */
        protected void cleanup(Context context
        ) throws IOException, InterruptedException {

        }

    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Configuration configuration = MapReduceTool.prepareCluster();

        String input = "/test";
        String output = "/test2";

        FileSystem fs = MapReduceTool.writeInputData(configuration, input,String.join("\n", "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 20 30 99".split(" ")));

        Class<?> jarClass = Filter.class;
        Class<? extends Mapper> mapClass = MapSide.class;
        Class<? extends Reducer> reduceClass = ReduceSide.class;

        Class<?> mapOutputKey = LongWritable.class;
        Class<?> mapOutputValue = MetricTuple.class;
        Class<?> outputKey = LongWritable.class;
        Class<?> outputValue = MetricTuple.class;

        MapReduceTool.doMapReduce(configuration, input, output, jarClass, mapClass, reduceClass, mapOutputKey, mapOutputValue, outputKey, outputValue);

        MapReduceTool.showOutput(fs, output);

        System.exit(0);

    }


}

