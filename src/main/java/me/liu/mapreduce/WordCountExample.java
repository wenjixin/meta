package me.liu.mapreduce;


import me.liu.function.Try;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by tongwei on 17/8/16.
 */
public class WordCountExample {

    public static class MapSide extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        @Override
        public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {

            StringTokenizer tokenizer = new StringTokenizer(value.toString());
            while (tokenizer.hasMoreTokens()) {
                word.set(tokenizer.nextToken());
                output.collect(word, one);
            }

        }
    }

    public static class ReduceSide extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {

        @Override
        public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {

            int sum = 0;

            while (values.hasNext()) {
                sum = sum + values.next().get();
            }

            output.collect(key, new IntWritable(sum));

        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Configuration configuration = new Configuration();

        ClusterManager clusterManager = new ClusterManager(configuration);
        clusterManager.startHdfs();

        FileSystem fs = FileSystem.get(configuration);
        OutputStream inputData = fs.create(new Path("/test"));
        inputData.write("hello world".getBytes());
        inputData.close();

        JobConf conf = new JobConf(configuration, WordCount.class);
        conf.setJobName("wordcount");
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(IntWritable.class);
        conf.setMapperClass(MapSide.class);
        conf.setCombinerClass(ReduceSide.class);
        conf.setReducerClass(ReduceSide.class);
        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);
        FileInputFormat.setInputPaths(conf, new Path("/test"));
        FileOutputFormat.setOutputPath(conf, new Path("/test2"));

        JobClient.runJob(conf);

        List<InputStream> iss = Arrays.asList(fs.listStatus(new Path("/test2")))
                .stream()
                .filter(f -> f.isFile())
                .flatMap(
                        Try.of(tmp -> Stream.of(fs.open(tmp.getPath())), Stream.empty())
                )
                .collect(Collectors.toList());

        System.out.println(IOUtils.toString(new SequenceInputStream(Collections.enumeration(iss))));

        System.exit(0);

    }


}

