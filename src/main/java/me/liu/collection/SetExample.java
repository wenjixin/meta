package me.liu.collection;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by tongwei on 17/7/26.
 */
public class SetExample {
    public static void main(String[] args) {
        HashSet<String> hashSet = new HashSet<>();

        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>();

        TreeSet<String> treeSet = new TreeSet<>();

        ConcurrentSkipListSet<String> concurrentSkipListSet;

        CopyOnWriteArraySet<String> copyOnWriteArraySet;

        Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
    }
}
