package me.liu.thread.synchronizer;


import java.util.concurrent.atomic.AtomicReference;

/**
 * MCS Spinlock 是一种基于链表的可扩展、高性能、公平的自旋锁，申请线程只在本地变量上自旋，直接前驱负责通知其结束自旋，从而极大地减少了不必要的处理器缓存同步的次数，降低了总线和内存的开销。
 * <p>
 * <p>
 * <p>
 * Created by tongwei on 17/9/5.
 */
public class MCSLock implements LLock {


    class MCSNode {

        volatile boolean locked;

        volatile MCSNode next;
    }

    private AtomicReference<MCSNode> tail = new AtomicReference<>(null);


    private ThreadLocal<MCSNode> current = new ThreadLocal<>();

    @Override
    public void lock() {

        MCSNode node = current.get();

        if (node == null) {
            node = new MCSNode();
            node.locked = true;
            current.set(node);
        }

        MCSNode pre = tail.getAndSet(node);

        if (pre == null) {
            return;
        } else {
            pre.next = node;
            while (node.locked) {

            }
        }

    }

    @Override
    public void unlock() {

        MCSNode node = current.get();

        if (node.next != null) {

            node.next.locked = false;

            current.set(null);

        } else {

            //两种情况

            if (tail.compareAndSet(node, null)) {//是最后一个线程了
                return;
            } else {//还有一个线程，但是还没有设置next指针,主要应对过了39行，但还没到44行的情况

                while (node.next == null) {

                }
                node.next.locked = false;

                current.set(null);

            }

        }

    }

    public static void main(String[] args) {
        MCSLock lock = new MCSLock();

        lock.lock();
        lock.unlock();
        lock.lock();
        lock.unlock();
    }


}


