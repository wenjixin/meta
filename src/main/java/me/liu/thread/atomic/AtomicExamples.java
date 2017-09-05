package me.liu.thread.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tongwei on 17/9/5.
 */
public class AtomicExamples {
    public static void main(String[] args) {

        AtomicInteger atomicInteger = new AtomicInteger();

        System.out.println(atomicInteger.get());

        System.out.println(atomicInteger.getAndIncrement());

        System.out.println(atomicInteger.incrementAndGet());

    }
}
