package me.liu.thread;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by tongwei on 17/8/28.
 */
public class ProducerConsumerExample {


}


class WaitNotifyQueue<T> implements IBlockingQueue<T> {

    private int queueSize;
    private final LinkedList<T> list = new LinkedList<T>();
    private final Object lock = new Object();


    //notify可能会导致死锁，而notifyAll则不会的例子


    @Override
    public void put(T data) throws InterruptedException {

        synchronized (lock) {  //获取lock对象的锁
            while (list.size() >= queueSize) {
                lock.wait();    //释放对象的锁，进入对象的锁池
            }
            list.addLast(data);
            lock.notifyAll();  //通知所有等待池中的对象，全部进入锁池，争夺锁的拥有权
        }

    }

    @Override
    public T take() throws InterruptedException {
        synchronized (lock) {
            while (list.size() <= 0) {
                lock.wait();
            }
            T data = list.removeFirst();
            lock.notifyAll();
            return data;
        }
    }
}

class ConditionBlockingQueue<T> implements IBlockingQueue<T> {

    private int queueSize = 10;
    private final LinkedList<T> list = new LinkedList<T>();

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();


    @Override
    public void put(T data) throws InterruptedException {
        lock.lock();

        try {

            while (queueSize == list.size()) {
                notFull.await();
            }

            list.addLast(data);
            notEmpty.signalAll();


        } finally {
            lock.unlock();
        }
    }

    @Override
    public T take() throws InterruptedException {
        lock.lock();

        try {

            while (0 == list.size()) {
                notEmpty.await();
            }
            T data = list.removeFirst();
            notFull.signal();
            return data;

        } finally {
            lock.unlock();
        }
    }
}


interface IBlockingQueue<T> {

    void put(T data) throws InterruptedException;

    T take() throws InterruptedException;
}