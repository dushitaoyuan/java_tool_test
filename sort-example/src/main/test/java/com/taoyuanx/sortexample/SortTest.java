package com.taoyuanx.sortexample;

import com.taoyuanx.sortexample.sort.Sort;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * @author dushitaoyuan
 * @desc 排序测试
 * @date 2020/4/7
 */
public class SortTest {
    private Integer[] array;
    private Integer[] descSorted;
    private Integer[] ascSorted;
    private Comparator<Integer> ascComparator = (x, y) -> {
        return x - y;
    };
    private Comparator<Integer> descComparator = ascComparator.reversed();

    @Before
    public void before() {
        array = new Integer[]{11, 6, 9, 6, 13, 7, 2, 11, 4, 17, 19, 17, 2, 6, 14};
        descSorted = Arrays.copyOf(array, array.length);
        Arrays.sort(descSorted, descComparator);
        ascSorted = Arrays.copyOf(array, array.length);
        Arrays.sort(ascSorted, ascComparator);
    }

    @Test
    public void bubbleSortTest() {
        Sort<Integer> bubbleSort = new BubbleSort<Integer>();
        Integer[] sort = bubbleSort.sort(array, descComparator);
        System.out.println(Arrays.toString(sort));
        System.out.println(Arrays.equals(descSorted, sort));
    }

    @Test
    public void selectSortTest() {
        Sort<Integer> selectSort = new SelectSort<>();
        Integer[] sort = selectSort.sort(array, descComparator);
        System.out.println(Arrays.toString(sort));
        System.out.println(Arrays.equals(descSorted, sort));
    }

    @Test
    public void insertSortTest() {
        Sort<Integer> integerSort = new InsertSort<>();
        Integer[] sort = integerSort.sort(array, ascComparator);
        System.out.println(Arrays.toString(sort));
        System.out.println(Arrays.equals(ascSorted, sort));
    }

    @Test
    public void sSort() {
        for (int i = 0; i < array.length; i++) {
            int select = i;
            for (int j = select + 1; j < array.length; j++) {
                if (array[select] > array[j]) {
                    select = j;
                }
            }
            if (i != select) {
                int temp = array[i];
                array[i] = array[select];
                array[select] = temp;
            }

        }
        System.out.println(Arrays.toString(array));
        System.out.println(Arrays.equals(ascSorted, array));
    }

    @Test
    public void shellSortTest() {
        Sort<Integer> shellSort = new ShellSort<>();
        Integer[] sort = shellSort.sort(array, descComparator);
        System.out.println(Arrays.toString(sort));
        System.out.println(Arrays.equals(descSorted, sort));
    }

    @Test
    public void mergeSortTest() {
        Sort<Integer> mergeSort = new MergeSort<Integer>();
        Integer[] sort = mergeSort.sort(array, descComparator);
        System.out.println(Arrays.toString(sort));
        System.out.println(Arrays.equals(descSorted, sort));
    }

    @Test
    public void quickSortTest() {
        Sort<Integer> quickSortTest = new QuickSort<>();
        Integer[] sort = quickSortTest.sort(array, ascComparator);
        System.out.println(Arrays.toString(sort));
        System.out.println(Arrays.equals(descSorted, sort));
    }

    @Test
    public void heapSortTest() {
        Sort<Integer> heapSortTest = new HeapSort<>();
        Integer[] sort = heapSortTest.sort(array, descComparator);
        System.out.println(Arrays.toString(sort));
        System.out.println(Arrays.equals(descSorted, sort));
        HeapSort<Integer> heapSortTest2 = new HeapSort<>();
        HeapSort.TreeNode<Integer> integerTreeNode = heapSortTest2.arrayToMaxHeap(sort, descComparator);
    }

    @Test
    public void countSortTest() {
        Sort<Integer> countSortTest = new CountSort();
        Integer[] sort = countSortTest.sort(array, descComparator);
        System.out.println(Arrays.toString(sort));
        System.out.println(Arrays.equals(descSorted, sort));
    }

    @Test
    public void bucketSortTest() {
        Sort<Integer> bucketSortTest = new BucketSort(5);
        Integer[] sort = bucketSortTest.sort(array, descComparator);
        System.out.println(Arrays.toString(sort));
        System.out.println(Arrays.equals(descSorted, sort));
    }

    @Test
    public void radixSortTest() {
        Sort<Integer> radixSort = new RadixSort();
        Integer[] sort = radixSort.sort(array, descComparator);
        System.out.println(Arrays.toString(sort));
        System.out.println(Arrays.equals(descSorted, sort));
    }

    @Test
    public void demoTest() {
        System.out.println(Math.round(1.5));
        System.out.println(Math.round(-1.6));
        List<String> list = new ArrayList<>();
        list.add("1");
        Iterator<String> iterator = list.iterator();
        ListIterator<String> stringListIterator = list.listIterator();
        stringListIterator.add("2");
        System.out.println(Arrays.toString(list.toArray()));

    }
}
