package me.liu.stream;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by tongwei on 17/5/9.
 */
public class StreamExample {

    public static void main(String[] args) {

        Stream<Double> stream = Stream.generate(Math::random);

        //System.out.println(stream.iterator().next().toString());

        System.out.println(stream.limit(1000).sorted().collect(Collectors.toList()));

    }
}
