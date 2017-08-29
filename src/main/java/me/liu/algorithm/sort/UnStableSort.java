package me.liu.algorithm.sort;

import java.util.Arrays;

/**
 * Created by tongwei on 17/8/4.
 */
public class UnStableSort {


    public static void main(String[] args) {

        //稳定性  值相等的在排序后保持顺序不变
        int[] a = {4, 10, 1, 2, 100, 3, -100000};

        heapSort(a);
        printArray(a);

        quickSortRecursive(a, 0, a.length - 1);
        printArray(a);

        selectionSort(a);
        printArray(a);

        shellSort(a);
        printArray(a);


    }


    /**
     * @param array
     */
    public static void shellSort(int[] array) {

        int step = array.length / 2;


        while (step > 0) {

            for (int i = 0; i < step; i++) {

                int j = i + step;

                while (j < array.length) {

                    for (int k = j - step; k >= i; k = k - step) {

                        if (array[k + step] < array[k]) {
                            swap(array, k + step, k);
                        } else {
                            break;
                        }
                    }
                    j = j + step;
                }
            }

            step = step / 2;

        }


    }


    public static void selectionSort(int[] array) {

        for (int i = 0; i < array.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < array.length; j++) {  //从后面未排序的数组中选出一个最小值和当前的值交换
                if (array[min] > array[j]) {
                    min = j;
                }
            }
            swap(array, min, i);
        }

    }

    private static void printArray(int[] array) {
        System.out.println(Arrays.toString(array));
    }

    public static void swap(int[] array, int x, int y) {

        int tmp = array[x];
        array[x] = array[y];
        array[y] = tmp;

    }

    public static void heapSort(int[] array) {

        //确定第一个非叶子节点
        int firstNoLeaf = (int) Math.floor((array.length - 1) / 2L);

        for (int i = firstNoLeaf; i >= 0; i--) {
            //从数组由右到左调整堆
            maxHeapify(array, i, array.length);
        }
        //最大堆

        for (int i = array.length - 1; i > 0; i--) {
            //将最大堆的根与最后未排序的元素交换
            swap(array, i, 0);
            //按照未排序的堆大小进行调整
            maxHeapify(array, 0, i);
        }

    }

    /**
     * 向下调整堆
     *
     * @param array
     * @param index 调整元素
     * @param size  堆大小
     */
    public static void maxHeapify(int[] array, int index, int size) {

        int left = index * 2 + 1;
        int right = index * 2 + 2;

        int max = index;

        if (left < size && array[max] < array[left]) {
            max = left;
        }
        if (right < size && array[max] < array[right]) {
            max = right;
        }

        if (max != index) { //找到左右子节点中最大的和父节点比
            swap(array, max, index);
            maxHeapify(array, max, size);//递归调用子节点，保证堆结构
        }

    }


    public static void quickSortRecursive(int[] array, int start, int end) {
        if (start >= end) {
            return;
        }
        int left = start;
        int right = end - 1;
        int chose = array[end];  //选择最后一个做为枢

        while (left < right) {  //分区

            while (array[left] <= chose && left < right) {
                left++;
            }

            while (array[right] >= chose && left < right) {
                right--;
            }

            int tmp = array[left];
            array[left] = array[right];
            array[right] = tmp;

        }

        if (array[left] >= chose) { //说明剩余的一个可以和枢互换
            int tmp = array[left];
            array[left] = array[end];
            array[end] = tmp;
        } else {    //说明所有元素都比枢小
            left++;
        }


        quickSortRecursive(array, start, left - 1);
        quickSortRecursive(array, left + 1, end);

    }
}


