package me.liu.algorithm.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tongwei on 17/8/4.
 */
public class StableSort {


    public static void main(String[] args) {

        //稳定性  值相等的在排序后保持顺序不变
        int[] a = {4, 10, 1, 2, 100, 3};
        printArray(insertSort(a));


        mergeSort(a, 0, a.length - 1);
        printArray(a);


        int[] array = {4, 5, 6, 1, 2, 3, 100000, -1, -1000000};

        mergeSortFor(array);
        printArray(array);

        bucketSort(array);
        printArray(array);

        bubbleSort(array);
        printArray(array);

        mergeSort(array, 0, array.length - 1);
        printArray(array);

        int[] array1 = {4, 5, 6, 1, 2, 3, 100000};
        radixSort(array1);
        printArray(array1);

    }

    private static void printArray(int[] a) {
        System.out.println(Arrays.toString(a));
    }


    public static void radixSort(int[] array) {

        //TODO 基数排序   固定位数

        int max = array[0];

        for (int i = 1; i < array.length; i++) {
            max = max > array[i] ? max : array[i];
        }
        int d = 1;
        int p = 10;
        while (max >= p) {
            max = max / 10;
            d++;
        }

        int bucket[] = new int[10];
        int tmp[] = new int[array.length];

        int radix = 1;

        for (int i = 1; i <= d; i++) {


            for (int j = 0; j < bucket.length; j++) {
                bucket[j] = 0;
            }

            for (int item : array) {
                bucket[(item / radix) % 10]++;
            }

            for (int j = 0; j < 9; j++) {
                bucket[j + 1] = bucket[j] + bucket[j + 1];
            }

            for (int j = array.length - 1; j >= 0; j--) {

                int index = (array[j] / radix) % 10;
                bucket[index] = bucket[index] - 1;
                tmp[bucket[index]] = array[j];
            }

            radix = radix * 10;
            System.arraycopy(tmp, 0, array, 0, array.length);

        }


    }

    public static void countingSort(int[] array) {

        //TODO 计数排序

    }


    public static void bucketSort(int[] array) {

        //O(n)；需要O(k)额外空间
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;

        for (int i = 0; i < array.length; i++) {

            if (max < array[i]) {
                max = array[i];
            }

            if (min > array[i]) {
                min = array[i];
            }

        }

        int step = 10;
        int k = max / step - min / step + 1;  //计算桶的个数

        List<List<Integer>> buckets = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            buckets.add(new ArrayList<>());
        }


        for (int i = 0; i < array.length; i++) {

            int indexOfBucket = array[i] / step - min / step;//定位放在哪个桶

            List<Integer> list = buckets.get(indexOfBucket);

            list.add(array[i]);

            for (int j = list.size() - 1; j > 0; j--) {//内部使用插入排序
                if (list.get(j) < list.get(j - 1)) {
                    int tmp = list.get(j);
                    list.set(j, list.get(j - 1));
                    list.set(j - 1, tmp);
                } else {
                    break;
                }
            }
        }

        int i = 0;
        for (List<Integer> bucket :
                buckets) {
            for (int item :
                    bucket) {
                array[i] = item;
                i++;
            }
        }

    }

    /**
     * 时间复杂度分析
     * <p>
     * <p>
     * T(n)=
     *
     * @param array
     * @return
     */
    public static int[] insertSort(int[] array) {

        // 插入排序只要最多O(n)交换

        //选择第一个元素是排好序的,第二个元素插入
        for (int i = 1; i < array.length; i++) {

            int tmp = array[i];

            //插入元素从右到左和有序数组比较
            for (int j = i - 1; j >= 0; j--) {

                if (tmp >= array[j]) {
                    //当插入元素的值大于当前值，说明找到合适的位置
                    break;

                } else {
                    //小于当前值，与当前值交换，注意插入元素下标已经变了
                    array[j + 1] = array[j];
                    array[j] = tmp;
                }

            }
        }

        return array;
    }


    /**
     * @param array
     */
    public static void bubbleSort(int array[]) {

        for (int i = 0; i < array.length - 1; i++) {

            //每轮将当前最大的数置顶，每次后交换的个数减一

            //冒泡排序需要  O(n^2)次交换
            for (int j = 0; j < array.length - 1 - i; j++) {

                if (array[j] > array[j + 1]) {

                    array[j] = array[j] ^ array[j + 1];
                    array[j + 1] = array[j] ^ array[j + 1];
                    array[j] = array[j] ^ array[j + 1];

                }

            }

        }

    }

    public static void mergeSortFor(int array[]) {

        int result[] = new int[array.length];

        for (int i = 1; i < array.length * 2; i *= 2) {

            for (int j = 0; j < array.length; j = j + i * 2) {


                int low = j;
                int mid = (j + i) < array.length ? (j + i) : array.length;
                int end = (j + 2 * i) < array.length ? (j + 2 * i) : array.length;
                int pointer1 = low;
                int pointer2 = mid;

                while (pointer1 < mid && pointer2 < end) {

                    if (array[pointer1] <= array[pointer2]) {
                        result[low] = array[pointer1];
                        pointer1++;
                        low++;
                    } else {
                        result[low] = array[pointer2];
                        pointer2++;
                        low++;
                    }
                }

                while (pointer1 < mid) {
                    result[low] = array[pointer1];
                    low++;
                    pointer1++;
                }

                while (pointer2 < end) {
                    result[low] = array[pointer2];
                    low++;
                    pointer2++;
                }

            }
            int[] tmp = array;
            array = result;
            result = tmp;

        }


    }

    public static void mergeSort(int array[], int start, int end) {


        // O(n log n) O(n)的额外空间
        /**
         * 处理只有一个元素的时候，递归的尽头
         */
        if (end == start) {
            return;
        }

        //选择分隔点
        int middle = (end - start) / 2 + start;

        // a[0....middle]
        mergeSort(array, start, middle);

        // a[middle+1....length-1]
        mergeSort(array, middle + 1, end);

        merge(array, start, middle, end);
    }

    public static void merge(int array[], int start, int middle, int end) {

        int result[] = new int[end - start + 1];
        int i = start, j = middle + 1, k = 0;
        while (i <= middle && j <= end) {
            if (array[i] <= array[j]) {
                result[k] = array[i];
                i++;
                k++;
            } else {
                result[k] = array[j];
                j++;
                k++;
            }
        }
        while (i <= middle) {
            result[k] = array[i];
            i++;
            k++;
        }

        while (j <= end) {
            result[k] = array[j];
            j++;
            k++;
        }

        //使用result更新原始数组
        System.arraycopy(result, 0, array, start, result.length);
    }
}


