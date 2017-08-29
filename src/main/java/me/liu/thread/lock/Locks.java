package me.liu.thread.lock;

import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

/**
 * Created by tongwei on 17/8/28.
 */
public class Locks {

    //synchronized
    //在同一个时间点，该同步锁能且只能被一个线程获取到。这样，获取到同步锁的线程就能进行CPU调度，从而在CPU上执行；而没有获取到同步锁的线程，必须进行等待，直到获取到同步锁之后才能继续运行


    //JUC

    Lock lock = new Lock() {
        @Override
        public void lock() {

        }

        @Override
        public void lockInterruptibly() throws InterruptedException {

        }

        @Override
        public boolean tryLock() {
            return false;
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public void unlock() {

        }

        @Override
        public Condition newCondition() {
            return null;
        }
    };


    ReadWriteLock readWriteLock = new ReadWriteLock() {
        @Override
        public Lock readLock() {
            return null;
        }

        @Override
        public Lock writeLock() {
            return null;
        }
    };


    //与lock结合使用，提供类似object wait notify的功能
    Condition condition = new Condition() {
        @Override
        public void await() throws InterruptedException {

        }

        @Override
        public void awaitUninterruptibly() {

        }

        @Override
        public long awaitNanos(long nanosTimeout) throws InterruptedException {
            return 0;
        }

        @Override
        public boolean await(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public boolean awaitUntil(Date deadline) throws InterruptedException {
            return false;
        }

        @Override
        public void signal() {

        }

        @Override
        public void signalAll() {

        }
    };

    static {

        ReentrantLock reentrantLock = new ReentrantLock();

        reentrantLock.newCondition();


        // 提供读锁和写锁
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

        ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();

        reentrantReadWriteLock.writeLock();
        reentrantReadWriteLock.isWriteLocked();
    }


    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        
        LockSupport.park();

        //只能用一次
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.countDown();
        countDownLatch.countDown();

        //等到所有线程到达
        countDownLatch.await();


        CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
        //每个线程调用await
        cyclicBarrier.await();


        Semaphore semaphore = new Semaphore(10, false);
        semaphore.acquire(10);
        semaphore.release();
        semaphore.release(5);

    }


}
