package me.liu.collection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by tongwei on 17/7/26.
 */
public class MapExample {

    public static void main(String[] args) {

        // 结合负载因子的定义公式可知，threshold就是在此Load factor和length(数组长度)对应下允许的最大元素数目，超过这个数目就重新resize(扩容)，
        // 扩容后的HashMap容量是之前容量的两倍。默认的负载因子0.75是对空间和时间效率的一个平衡选择，建议大家不要修改，除非在时间和空间比较特殊的情况下，
        // 如果内存空间很多而又对时间效率要求很高，可以降低负载因子Load factor的值；
        // 相反，如果内存空间紧张而对时间效率要求不高，可以增加负载因子loadFactor的值，这个值可以大于1。


        HashMap<String, String> map = new HashMap<>(16, 1);
        //


        map.put("", "");
        map.get("");

        //设计者认为这方法很容易发生碰撞。为什么这么说呢？不妨思考一下，在n - 1为15(0x1111)时，其实散列真正生效的只是低4bit的有效位，当然容易碰撞了。

        //因此，设计者想了一个顾全大局的方法(综合考虑了速度、作用、质量)，就是把高16bit和低16bit异或了一下。设计者还解释到因为现在大多数的hashCode的分布已经很不错了，就算是发生了碰撞也用O(logn)的tree去做了。仅仅异或一下，既减少了系统的开销，也不会造成的因为高位没有参与下标的计算(table长度比较小时)，从而引起的碰撞。


        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>(16, 0.75f, true);
        linkedHashMap.put("1", "");
        linkedHashMap.put("2", "");
        linkedHashMap.get("2");
        linkedHashMap.get("1");
        System.out.println(linkedHashMap);

        //红黑树，是一个平衡排序树 操作时间复杂度 O（logn）
        TreeMap<String, String> treeMap = new TreeMap<>();

        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();

        ConcurrentSkipListMap<String, String> concurrentSkipListMap = new ConcurrentSkipListMap<>();

    }
}
