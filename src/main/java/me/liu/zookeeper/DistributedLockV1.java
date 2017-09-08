package me.liu.zookeeper;

import me.liu.thread.synchronizer.LLock;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 当前模式下会出现惊群现象
 * Created by tongwei on 17/9/7.
 */
public class DistributedLockV1 implements LLock {


    static String LOCK = "/LOCK";

    CuratorFramework client;

    NodeCache node;

    @Override
    public void lock() {

        node = new NodeCache(client, LOCK);

        final Thread thread = Thread.currentThread();

        try {
            node.getListenable().addListener(new NodeCacheListener() {
                @Override
                public void nodeChanged() throws Exception {

                    if (node.getCurrentData() == null) {

                        System.out.println(thread.getId() + " find change ");
                        thread.interrupt();

                    } else {
                        //System.out.println(node.getCurrentData());
                    }

                }
            });
            node.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {

            try {

                client.create().withMode(CreateMode.EPHEMERAL).forPath(LOCK);

                return;

            } catch (KeeperException e) {

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e1) {
                    System.out.println(Thread.currentThread().getId() + " thread is notify");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    @Override
    public void unlock() {
        try {
            client.delete().forPath(LOCK);
            node.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public DistributedLockV1(String zkStr) {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

        client =
                CuratorFrameworkFactory.builder()
                        .connectString(zkStr)
                        .sessionTimeoutMs(5000)
                        .connectionTimeoutMs(5000)
                        .retryPolicy(retryPolicy)
                        .build();

        client.start();

    }

    public static void main(String[] args) throws Exception {

        TestingServer server = new TestingServer();

        server.start();

        ExecutorService service = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 5; i++) {
            service.execute(() -> {
                DistributedLockV1 test = new DistributedLockV1(server.getConnectString());
                try {
                    test.lock();
                    System.out.println(Thread.currentThread().getId() + " hold the lock");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                test.unlock();

                System.out.println(Thread.currentThread().getId() + " unlock");
            });
        }


        service.shutdown();

        while (!service.isTerminated()) {
            Thread.sleep(1000);
        }
        System.exit(0);

    }


}
