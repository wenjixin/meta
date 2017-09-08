package me.liu.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by tongwei on 17/9/7.
 */
public class ZKServer {

    public static void main(String[] args) throws Exception {

        TestingServer server = new TestingServer();

        server.start();

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client =
                CuratorFrameworkFactory.builder()
                        .connectString(server.getConnectString())
                        .sessionTimeoutMs(5000)
                        .connectionTimeoutMs(5000)
                        .retryPolicy(retryPolicy)
                        .build();

        client.start();

        client.blockUntilConnected();

        EQNode(client);
        nodeCache(client);

        interProcessMutex(client);

        TimeUnit.SECONDS.sleep(2);
        System.exit(0);
    }

    private static void interProcessMutex(CuratorFramework client) {
        Executor executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {

            executor.execute(() -> {

                InterProcessMutex interProcessMutex = new InterProcessMutex(client, "/lock");
                try {
                    interProcessMutex.acquire();
                    System.out.println(Thread.currentThread().getId() + " acquire the lock");
                    interProcessMutex.release();
                    System.out.println(Thread.currentThread().getId() + " release the lock");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
        }
    }

    private static void EQNode(CuratorFramework client) throws Exception {

        String node = client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/locktest/lock-", "hello".getBytes());
        client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/locktest/lock-", "hello".getBytes());

        System.out.println(node);

        System.out.println(client.getChildren().forPath("/locktest"));

        System.out.println(ZKPaths.getNodeFromPath(node));

    }


    private static void nodeCache(CuratorFramework client) throws Exception {

        final NodeCache cache = new NodeCache(client, "/lock");
        cache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                ChildData data = cache.getCurrentData();
                if (null != data) {
                    System.out.println("节点数据：" + new String(cache.getCurrentData().getData()));
                } else {
                    System.out.println("节点被删除!");
                }

            }
        });
        cache.start();

    }
}
