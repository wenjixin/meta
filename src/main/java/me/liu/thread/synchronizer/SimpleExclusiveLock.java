package me.liu.thread.synchronizer;

import java.util.Random;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Created by tongwei on 17/9/4.
 */
public class SimpleExclusiveLock extends AbstractQueuedSynchronizer implements LLock {

    protected boolean tryAcquire(int arg) {
        if (compareAndSetState(0, 1)) {
            setExclusiveOwnerThread(Thread.currentThread());
            return true;
        }
        return false;
    }

    protected boolean tryRelease(int arg) {
        setExclusiveOwnerThread(null);
        setState(0);
        return true;
    }

    public void lock() {
        acquire(1);
    }

    public boolean tryLock() {
        return tryAcquire(1);
    }

    public void unlock() {
        release(1);
    }

    public boolean isLocked() {
        return isHeldExclusively();
    }

    public static void main(String[] args) throws InterruptedException {


        LLock lock = new MCSLock();
        testLock(lock);
//
//        lock = new SpinLock();
//        testLock(lock);

//        lock = new CLHLock();
//        testLock(lock);


    }

    private static void testLock(final LLock lock) {

        lock.lock();
        System.out.println(lock.getClass() + " main thread lock!");

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {

//                    try {
//                        Thread.sleep(new Random().nextInt(100));
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }

                    lock.lock();

                    System.out.println(Thread.currentThread().getId() + " acquired the lock!");

                    lock.unlock();

                    System.out.println(Thread.currentThread().getId() + " unlock the lock!");


//                    try {
//                        Thread.sleep(new Random().nextInt(100));
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }

                    lock.lock();

                    System.out.println(Thread.currentThread().getId() + " acquired the lock!");

                    lock.unlock();

                    System.out.println(Thread.currentThread().getId() + " unlock the lock!");


                }
            }).start();
            // 简单的让线程按照for循环的顺序阻塞在lock上
        }

        System.out.println(lock.getClass() + " main thread unlock!");
        lock.unlock();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class LReadLock extends AbstractQueuedSynchronizer implements LLock {

    @Override
    public void lock() {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public void unlock() {

    }
}

class LWriteLock extends AbstractQueuedSynchronizer implements LLock {

    @Override
    public void lock() {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public void unlock() {

    }
}

