package me.liu.mapreduce;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

/**
 * Created by tongwei on 17/8/16.
 */
public class Topk {


    public static class MapSide extends Mapper<LongWritable, Text, NullWritable, Text> {


        private TreeMap<Text, Text> treeMap = new TreeMap<>();
        private int topk;

        /**
         * Called once at the beginning of the task.
         */
        protected void setup(Context context
        ) throws IOException, InterruptedException {
            // NOTHING
            topk = context.getConfiguration().getInt("topk", 3);
        }

        /**
         * Called once for each key/value pair in the input split. Most applications
         * should override this, but the default is the identity function.
         */
        @SuppressWarnings("unchecked")
        protected void map(LongWritable key, Text value,
                           Context context) throws IOException, InterruptedException {

            System.out.println("map key " + key.get());
            treeMap.put(new Text(value), new Text(value));

            if (treeMap.size() > topk) {
                treeMap.remove(treeMap.firstKey());
            }
        }

        /**
         * Called once at the end of the task.
         */
        protected void cleanup(Context context
        ) throws IOException, InterruptedException {

            for (Text text :
                    treeMap.values()) {
                context.write(NullWritable.get(), text);
            }
        }

    }

    public static class ReduceSide extends Reducer<NullWritable, Text, NullWritable, Text> {


        private TreeMap<Text, Text> treeMap = new TreeMap<>();
        private int topk;

        /**
         * Called once at the start of the task.
         */
        protected void setup(Context context
        ) throws IOException, InterruptedException {
            topk = context.getConfiguration().getInt("topk", 3);
        }

        /**
         * This method is called once for each key. Most applications will define
         * their reduce class by overriding this method. The default implementation
         * is an identity function.
         */
        @SuppressWarnings("unchecked")
        protected void reduce(NullWritable key, Iterable<Text> values, Context context
        ) throws IOException, InterruptedException {

            for (Text text :
                    values) {
                treeMap.put(new Text(text), new Text(text));
                if (treeMap.size() > topk) {
                    treeMap.remove(treeMap.firstKey());
                }
            }
        }

        /**
         * Called once at the end of the task.
         */
        protected void cleanup(Context context
        ) throws IOException, InterruptedException {
            for (Text text :
                    treeMap.values()) {
                context.write(NullWritable.get(), text);
            }
        }

    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Configuration configuration = MapReduceTool.prepareCluster();

        String input = "/test";
        String output = "/test2";

        FileSystem fs = MapReduceTool.writeInputData(configuration, input, String.join("\n", "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 20 30 99".split(" ")));

        Class<?> jarClass = Topk.class;
        Class<? extends Mapper> mapClass = MapSide.class;
        Class<? extends Reducer> reduceClass = ReduceSide.class;

        Class<?> mapOutputKey = NullWritable.class;
        Class<?> mapOutputValue = Text.class;
        Class<?> outputKey = NullWritable.class;
        Class<?> outputValue = Text.class;

        MapReduceTool.doMapReduce(configuration, input, output, jarClass, mapClass, reduceClass, mapOutputKey, mapOutputValue, outputKey, outputValue);

        MapReduceTool.showOutput(fs, output);

        System.exit(0);

    }


}

