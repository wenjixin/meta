package me.liu.zookeeper;

import me.liu.thread.synchronizer.LLock;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

/**
 * curator实现的简版
 * Created by tongwei on 17/9/7.
 */
public class DistributedLockV2 implements LLock {


    static String LOCK = "/LOCK";

    CuratorFramework client;

    String myNode = null;

    Thread thread = Thread.currentThread();

    @Override
    public void lock() {


        try {

            myNode = client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(LOCK + "/lock-");

            boolean hasLock = false;

            while (!hasLock) {

                List<String> childrens = client.getChildren().forPath(LOCK);
                Collections.sort(childrens);

                String nodeFromPath = ZKPaths.getNodeFromPath(myNode);
                int myIndex = childrens.indexOf(nodeFromPath);

                if (nodeFromPath.equals(childrens.get(0))) {

                    hasLock = true;

                    return;
                }

                try {

                    client.getData().usingWatcher(new CuratorWatcher() {
                        @Override
                        public void process(WatchedEvent event) throws Exception {

                            notifyFromWatcher();

                        }
                    }).forPath(ZKPaths.makePath(LOCK, childrens.get(myIndex - 1)));

                    LockSupport.park(thread);


                } catch (KeeperException e) {

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private synchronized void notifyFromWatcher() {

        System.out.println(thread.getId() + " find lock change");

        LockSupport.unpark(thread);
    }


    @Override
    public void unlock() {

        if (myNode != null) {

            try {
                client.delete().forPath(myNode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            client.delete().forPath(LOCK);
        } catch (Exception e) {
            // e.printStackTrace();
        }


    }


    public DistributedLockV2(String zkStr) {

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
                DistributedLockV2 test = new DistributedLockV2(server.getConnectString());
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
