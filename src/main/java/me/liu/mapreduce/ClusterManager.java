package me.liu.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.yarn.server.MiniYARNCluster;

import java.io.IOException;

/**
 * Created by tongwei on 17/8/16.
 */
public class ClusterManager {

    private Configuration configuration;

    public ClusterManager(Configuration configuration) {
        this.configuration = configuration;
    }


    public void startAll() {

        try {
            startHdfs();
            startYarn();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void startHdfs() throws IOException {
        MiniDFSCluster cluster = new MiniDFSCluster.Builder(configuration)
                .build();
    }


    public void startYarn() throws IOException {
        MiniYARNCluster cluster = new MiniYARNCluster("test", 2, 1, 1);
        cluster.init(configuration);
    }

}
