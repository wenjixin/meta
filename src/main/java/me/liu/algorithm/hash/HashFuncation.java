package me.liu.algorithm.hash;

/**
 * Hash函数标准：
 * <p>
 * 1.散列函数的输出值尽量接近均匀分布；   y~U[0, m]
 * 2.x的微小变化可以使f(x)发生非常大的变化，即所谓“雪崩效应”(Avalanche effect)； |df(x)/dx| >> 1；
 * <p>
 * Created by tongwei on 17/8/28.
 */
public class HashFuncation {


    /**
     * p.无冲突，简单，均匀
     * n.需要知道关键字分布，适合小且连续的情况
     *
     * @param key
     * @return
     */
    public static int directAddressHash(int key) {

        int a = 1;
        int b = 2;

        return a * key + b;

    }

    /**
     * 使用除留余数法的一个经验是，若散列表表长为m，通常p为小于或等于表长（最好接近m)的最小质数或不包含小于20质因子的合数。
     *
     * @param key
     * @return
     */
    public static int modHash(int key) {
        int p = 97;

        return key % p;
    }


    /**
     * 平方后取其中几位,取的位数又hash表长度决定
     *
     * @param key
     * @return
     */
    public static int sqrt(int key) {

        return ((int) Math.sqrt(key));

    }


    /**
     * 1. hash(i) = (hash(key)+d(i))mod m
     * i是冲突次数  di为序列，可为线性，平方，伪随机等方式
     * <p>
     * 会有一种情况：Cluster 堆积现象（不冲突的后面冲突了）
     * 删除的时候不好删除，会导致截断在它之后填人散列表的同义词结点的查找路径
     *
     * @return
     */
    public static int openAddressing() {
        return 0;
    }


    /**
     * @return
     */
    public static int linkedList() {
        return 0;
    }

    /**
     * 不易产生堆积
     *
     * @return
     */
    public static int reHash() {
        return 0;
    }


    public static int quickHash(int key) {

        return (key | 0X0000FFFF) ^ (key >> 16);

    }


    /**
     * linux 内核中的实现
     * 1.不要退化为链表
     * 2.单项的，不要被简单的方法就能预测
     *
     * @return
     */
    public static int jhash() {
        return 0;
    }


    public static void main(String[] args) {


        for (int i = 0; i < 1000; i++) {
            System.out.println(quickHash(i));
        }


    }

}
