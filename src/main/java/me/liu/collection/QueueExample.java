package me.liu.collection;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.concurrent.*;

/**
 * Created by tongwei on 17/7/26.
 */
public class QueueExample {
    public static void main(String[] args) {

        LinkedList linkedList = new LinkedList();

        ArrayDeque arrayDeque = new ArrayDeque();

        System.out.println(arrayDeque.peek());
        arrayDeque.push(1);


        PriorityQueue priorityQueue;

        ConcurrentLinkedQueue concurrentLinkedQueue = new ConcurrentLinkedQueue();
        System.out.println(concurrentLinkedQueue.peek());
        System.out.println(concurrentLinkedQueue.poll());


//定长 循环数组 一把公共锁  两个非空 非满 的条件用于阻塞的情况
        ArrayBlockingQueue arrayBlockingQueue;

//链表实现  takeLock putLock notEmpty notFull

        // 这个主要是循环队列的原因，主要是数组和链表不同，链表队列的添加和头部的删除，都是只和一个节点相关，添加只往后加就可以，
        // 删除只从头部去掉就好。为了防止head和tail相互影响出现问题，这里就需要原子性的计数器，头部要移除，
        // 首先得看计数器是否大于0，每个添加操作，都是先加入队列，然后计数器加1，这样保证了，队列在移除的时候，
        // 长度是大于等于计数器的，通过原子性的计数器，双锁才能互不干扰。数组的一个问题就是位置的选择没有办法原子化，
        // 因为位置会循环，走到最后一个位置后就返回到第一个位置，这样的操作无法原子化，所以只能是加锁来解决。


        LinkedBlockingQueue linkedBlockingQueue;


        // DelayQueue = BlockingQueue + PriorityQueue + Delayed

        DelayQueue<DelayedObject> delayQueue = new DelayQueue<DelayedObject>();

        delayQueue.poll();


    }
}

class DelayedObject implements Delayed {

    @Override
    public long getDelay(TimeUnit unit) {
        return 0;
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }
}
