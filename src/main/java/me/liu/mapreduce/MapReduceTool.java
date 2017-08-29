package me.liu.mapreduce;

import me.liu.function.Try;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by tongwei on 17/8/16.
 */
public class MapReduceTool {


    static Configuration prepareCluster() throws IOException {

        Configuration configuration = new Configuration();

        ClusterManager clusterManager = new ClusterManager(configuration);
        clusterManager.startHdfs();
        return configuration;
    }

    static void doMapReduce(Configuration configuration, String input, String output, Class<?> jarClass, Class<? extends Mapper> mapClass, Class<? extends Reducer> reduceClass, Class<?> mapOutputKey, Class<?> mapOutputValue, Class<?> outputKey, Class<?> outputValue) throws IOException, InterruptedException, ClassNotFoundException {

        Job job = Job.getInstance(configuration);
        // 指定程序的入口
        job.setJarByClass(jarClass);

        job.getConfiguration().setInt("topk", 5);
        // 指定自定义的Mapper阶段的任务处理类
        job.setMapperClass(mapClass);
        job.setMapOutputKeyClass(mapOutputKey);
        job.setMapOutputValueClass(mapOutputValue);
        job.setInputFormatClass(TextInputFormat.class);
        // 数据HDFS文件服务器读取数据路径
        FileInputFormat.setInputPaths(job, new Path(input));

        // 指定自定义的Reducer阶段的任务处理类
        job.setReducerClass(reduceClass);
        // 设置最后输出结果的Key和Value的类型
        job.setOutputKeyClass(outputKey);
        job.setOutputValueClass(outputValue);
        job.setOutputFormatClass(TextOutputFormat.class);

        // 将计算的结果上传到HDFS服务
        FileOutputFormat.setOutputPath(job, new Path(output));

        // 执行提交job方法，直到完成，参数true打印进度和详情
        job.waitForCompletion(true);
    }

    static FileSystem writeInputData(Configuration configuration, String input, String data) throws IOException {
        FileSystem fs = FileSystem.get(configuration);
        OutputStream inputData = fs.create(new Path(input));
        String inputDatas = data;
        inputData.write(inputDatas.getBytes());
        inputData.close();
        return fs;
    }

    static void showOutput(FileSystem fs, String output) throws IOException {

        List<InputStream> iss = Arrays.asList(fs.listStatus(new Path(output)))
                .stream()
                .filter(f -> f.isFile())
                .flatMap(
                        Try.of(tmp -> Stream.of(fs.open(tmp.getPath())), Stream.empty())
                )
                .collect(Collectors.toList());

        System.out.println(IOUtils.toString(new SequenceInputStream(Collections.enumeration(iss))));
    }
}
