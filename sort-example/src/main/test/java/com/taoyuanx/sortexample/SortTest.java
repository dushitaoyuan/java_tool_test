package com.taoyuanx.sortexample;

import com.taoyuanx.sortexample.sort.Sort;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author dushitaoyuan
 * @desc 排序测试
 * @date 2020/4/7
 */
public class SortTest {
    private Integer[] array;
    private Integer[] sorted;
    private  Comparator<Integer> asccComparator = (x, y) -> {
        return x - y;
    };
    private Comparator<Integer> descComparator = asccComparator.reversed();
    @Before
    public void before() {
        array = new Integer[]{11, 6, 9, 6, 13, 7, 2, 11, 4, 17, 19, 17, 2, 6, 14};
        sorted = new Integer[]{19, 17, 17, 14, 13, 11, 11, 9, 7, 6, 6, 6, 4, 2, 2};

        array = new Integer[]{1,2,3,4,5};
        sorted = new Integer[]{5,4,3,2,1};
    }

    @Test
    public void bubbleSortTest() {
        Sort<Integer> bubbleSort = new BubbleSort<Integer>();
        Integer[] sort = bubbleSort.sort(array, descComparator);
        System.out.println(Arrays.toString(sort));
        System.out.println(Arrays.equals(sorted,sort));
    }
    @Test
    public void selectSortTest() {
        Sort<Integer> selectSort = new SelectSort<>();
        Integer[] sort = selectSort.sort(array, descComparator);
        System.out.println(Arrays.toString(sort));
        System.out.println(Arrays.equals(sorted,sort));
    }

    @Test
    public void insertSortTest() {
        Sort<Integer> integerSort = new InsertSort<>();
        Integer[] sort = integerSort.sort(array, descComparator);
        System.out.println(Arrays.toString(sort));
        System.out.println(Arrays.equals(sorted,sort));
    }

    @Test
    public void shellSortTest() {
        Sort<Integer> shellSort = new ShellSort<>();
        Integer[] sort = shellSort.sort(array, descComparator);
        System.out.println(Arrays.toString(sort));
        System.out.println(Arrays.equals(sorted,sort));
    }

    @Test
    public void mergeSortTest() {
        Sort<Integer> mergeSort = new MergeSort<Integer>();
        Integer[] sort = mergeSort.sort(array, descComparator);
        System.out.println(Arrays.toString(sort));
        System.out.println(Arrays.equals(sorted,sort));
    }

    @Test
    public void quickSortTest() {
        Sort<Integer> quickSortTest = new QuickSort<>();
        Integer[] sort = quickSortTest.sort(array, descComparator);
        System.out.println(Arrays.toString(sort));
        System.out.println(Arrays.equals(sorted,sort));
    }
}
