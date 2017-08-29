package me.liu.util;

import java.util.Objects;

/**
 * Created by tongwei on 17/7/26.
 */
public class Hash {

    private int filed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hash hash = (Hash) o;
        return filed == hash.filed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(filed);
    }

    public static void main(String[] args) {

        testObjectHash();

        testHashMapHash();
    }

    private static void testObjectHash() {

        Hash hash = new Hash();

        System.out.println(hash.hashCode());

    }


    private static void testHashMapHash() {

        Hash hash = new Hash();

        int hashCode = hash.hashCode();

        System.out.println(hashCode >>> 16);

        //高16位和低16位异或
        System.out.println(hashCode ^ (hashCode >>> 16));

    }
}
