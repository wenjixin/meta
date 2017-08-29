package me.liu.algorithm.hash;

import java.util.Objects;

/**
 * Created by tongwei on 17/8/28.
 */
public class KeyExample {

    private int field;

    private int field1;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyExample)) return false;
        KeyExample that = (KeyExample) o;
        return field == that.field &&
                field1 == that.field1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, field1);
    }

    public static void main(String[] args) {
        Integer a = new Integer(1);

        System.out.println(a.hashCode());


        System.out.println(new KeyExample().hashCode());
    }
}
