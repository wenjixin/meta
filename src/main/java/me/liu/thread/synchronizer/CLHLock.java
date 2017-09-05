package me.liu.thread.synchronizer;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 解决的问题：
 * <p>
 * <p>
 * Created by tongwei on 17/9/5.
 */
public class CLHLock implements LLock {

    private AtomicReference<CLHNode> tail = new AtomicReference<>(new CLHNode());

    private ThreadLocal<CLHNode> current = new ThreadLocal<>();


    public CLHLock() {
    }

    @Override
    public void lock() {

        CLHNode node = current.get();  //当前线程绑定的

        if (node == null) {
            node = new CLHNode();
            current.set(node);
        }

        node.locked = true;    //当前锁定

        CLHNode preN = tail.getAndSet(node);  //原子的获取和更新链表的队尾

        while (preN.locked) { //前驱释放锁或者第一个获取到lock的话，退出自旋

        }


    }

    @Override
    public void unlock() {

        CLHNode node = current.get();
        node.locked = false;  //通知后面的节点可以加锁了

        current.set(null);

    }

    public static class CLHNode {
        volatile boolean locked = false;
    }


    public static void main(String[] args) {
        CLHLock lock = new CLHLock();

        lock.lock();
        lock.unlock();
        lock.lock();
        lock.unlock();
    }

}
