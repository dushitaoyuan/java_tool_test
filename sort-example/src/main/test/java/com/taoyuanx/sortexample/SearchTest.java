package com.taoyuanx.sortexample;

import com.taoyuanx.sortexample.search.BinarySearch;
import com.taoyuanx.sortexample.search.DValueSearch;
import com.taoyuanx.sortexample.search.FibonacciSearch;
import com.taoyuanx.sortexample.search.SequenceSearch;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * @author dushitaoyuan
 * @desc 查找测试
 * @date 2020/5/6
 */
public class SearchTest {
    private Integer[] array;
    private Integer[] sortedArray;

    @Before
    public void before() {
        array = new Integer[]{11, 6, 9, 6, 13, 7, 2, 11, 4, 17, 19, 17, 2, 6, 14};
        sortedArray = Arrays.copyOf(array, array.length);
        Arrays.sort(sortedArray);
    }

    @Test
    public void sequenceSearchTest() {
        Integer findData = array[new Random().nextInt(array.length)];
        SequenceSearch search = new SequenceSearch();
        System.out.println(String.format("%s 位置为%s", findData, search.search(array, findData)));
    }

    @Test
    public void binarySearchTest() {
        Integer findData = sortedArray[new Random().nextInt(sortedArray.length)];
        BinarySearch search = new BinarySearch();
        System.out.println(Arrays.toString(sortedArray));
        System.out.println(String.format("%s 位置为%s", findData, search.search(sortedArray, findData)));
    }

    @Test
    public void dValueSearchTest() {
        /**
         * 通过比较得知：
         * 差值查找对于均匀有序，优于二分查找
         */
        DValueSearch search = new DValueSearch();
        Integer findData = sortedArray[new Random().nextInt(sortedArray.length)];
        System.out.println(Arrays.toString(sortedArray));
        System.out.println(String.format("%s 位置为%s", findData, search.search(sortedArray, findData)));

    }

    @Test
    public void fibonacciSearchTest() {

        FibonacciSearch search = new FibonacciSearch();
        Integer findData = sortedArray[new Random().nextInt(sortedArray.length)];
        System.out.println(Arrays.toString(sortedArray));
        System.out.println(String.format("%s 位置为%s", findData, search.search(sortedArray, findData)));

    }

    @Test
    public void dValueAndBinarySearchTest() {
        /**
         * 通过比较得知：
         * 差值查找对于均匀有序，优于二分查找
         * 分布越不均匀，差值查找越慢（趋势波浪递增）
         */
        Integer[] sortedArray = new Integer[]{2, 3, 4, 5, 6, 7, 8, 9};
        DValueSearch search = new DValueSearch();
        BinarySearch binarySearch = new BinarySearch();
        for (int i = 0; i < sortedArray.length; i++) {
            Integer findData = sortedArray[i];
            System.out.println(String.format("%s 位置为%s", findData, search.search(sortedArray, findData)));
            System.out.println(String.format("%s 位置为%s", findData, binarySearch.search(sortedArray, findData)));
        }
        search.sayStat();
        binarySearch.sayStat();

        System.out.println("-------------------------------------------------------");
        search.restStat();
        binarySearch.restStat();
        sortedArray = new Integer[]{1, 2, 3, 4, 7, 8, 10, 20, 31, 45, 66, 89, 90};
        for (int i = 0; i < sortedArray.length; i++) {
            Integer findData = sortedArray[i];
            System.out.println(String.format("%s 位置为%s", findData, search.search(sortedArray, findData)));
            System.out.println(String.format("%s 位置为%s", findData, binarySearch.search(sortedArray, findData)));
        }
        search.sayStat();
        binarySearch.sayStat();

    }
}
