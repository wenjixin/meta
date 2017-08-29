package me.liu.thread;

import java.util.concurrent.*;

/**
 * Created by tongwei on 17/8/28.
 */
public class ThreadPool {

    public static void main(String[] args) {


        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 15, 5L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardOldestPolicy());


        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {

            }
        });

        Executors.newCachedThreadPool();

        //适用的场景：1、很多短异步任务 2.超过60分钟销毁线程
        new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(), new ThreadPoolExecutor.AbortPolicy());

        Executors.newFixedThreadPool(10);


        //1.固定大小 2、无界的任务队列
        new ThreadPoolExecutor(10, 10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());

        //Executors.newScheduledThreadPool(10);


        Executors.newSingleThreadExecutor();

//        new Executors.FinalizableDelegatedExecutorService
//                (new ThreadPoolExecutor(1, 1,
//                        0L, TimeUnit.MILLISECONDS,
//                        new LinkedBlockingQueue<Runnable>()));


        Executors.newWorkStealingPool(10);


        new ForkJoinPool
                (10,
                        ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                        null, true);

    }
}
