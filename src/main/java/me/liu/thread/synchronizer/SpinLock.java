package me.liu.thread.synchronizer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by tongwei on 17/9/5.
 */
public class SpinLock implements LLock {

    //这种实现方式不能实现可重入锁
    private AtomicReference<Thread> owner = new AtomicReference<>();

    @Override
    public void lock() {

        Thread currentThread = Thread.currentThread();

        while (!owner.compareAndSet(null, currentThread)) { //如果锁没有被占用，则占用锁
            //自旋
        }

    }

    @Override
    public void unlock() {

        Thread currentThread = Thread.currentThread();

        owner.compareAndSet(currentThread, null);//只有自己锁定的才可以释放锁

    }

    public static void main(String[] args) {

        final LLock lock = new TicketLock();
        lock.lock();
        lock.lock();//查看是否为不可重入锁

        lock.unlock();


        ReentrantLock lock1 = new ReentrantLock();
        lock1.lock();
        lock1.lock();

        lock1.unlock();
        lock1.lock();
        lock1.lock();

    }
}


/**
 * 公平锁
 * <p>
 * Ticket Lock 虽然解决了公平性的问题，但是多处理器系统上，每个进程/线程占用的处理器都在读写同一个变量serviceNum ，
 * 每次读写操作都必须在多个处理器缓存之间进行缓存同步，这会导致繁重的系统总线和内存的流量，大大降低系统整体的性能。
 */
class TicketLock implements LLock {

    private AtomicInteger serviceNum = new AtomicInteger();
    private AtomicInteger ticketNum = new AtomicInteger();

    private ThreadLocal<Integer> ticket = new ThreadLocal<>();

    public TicketLock() {

    }

    @Override
    public void lock() {

        Integer myTicket = ticket.get();

        if (myTicket == null) {
            myTicket = ticketNum.getAndIncrement();
            ticket.set(myTicket);
        }

        while (myTicket != serviceNum.get()) {
            //自旋
        }

    }

    @Override
    public void unlock() {

        int next = ticket.get() + 1;

        serviceNum.compareAndSet(ticket.get(), next);  //自己释放自己

    }
}