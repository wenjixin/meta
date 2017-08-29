package me.liu.function;


/**
 * Created by tongwei on 17/8/16.
 */
@FunctionalInterface
public interface UncheckedFunction<T, R> {

    R apply(T t) throws Exception;


}
