package me.liu.thread.synchronizer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by tongwei on 17/9/5.
 */
public interface LLock extends Lock {

    default Condition newCondition() {
        return null;
    }

    default boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    default boolean tryLock() {
        return false;
    }

    default void lockInterruptibly() throws InterruptedException {

    }
}
