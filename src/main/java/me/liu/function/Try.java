package me.liu.function;

import java.util.Objects;
import java.util.function.Function;

/**
 * Created by tongwei on 17/8/16.
 */
public class Try {

    public static <T, R> Function<T, R> of(UncheckedFunction<T, R> mapper) {

        Objects.requireNonNull(mapper);

        return t -> {
            try {
                return mapper.apply(t);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };

    }

    public static <T, R> Function<T, R> of(
            UncheckedFunction<T, R> mapper, R defaultR) {

        Objects.requireNonNull(mapper);

        return t -> {
            try {
                return mapper.apply(t);
            } catch (Exception ex) {
                return defaultR;
            }
        };

    }


}
