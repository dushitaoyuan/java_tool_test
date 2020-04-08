package com.taoyuanx.sortexample;

import com.taoyuanx.sortexample.sort.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author dushitaoyuan
 * @date 2020/4/7
 * @desc 归并排序
 * 把长度为n的输入序列分成两个长度为n/2的子序列；
 * 对这两个子序列分别采用归并排序；
 * 将两个排序好的子序列合并成一个最终的排序序列。
 */
public class MergeSort<T> implements Sort<T> {


    @Override
    public T[] sort(T[] array, Comparator comparator) {
        if (array.length < 2) {
            return array;
        }
        int mid = array.length / 2;
        T[] leftArray = sort(Arrays.copyOfRange(array, 0, mid), comparator);
        T[] rightArray = sort(Arrays.copyOfRange(array, mid, array.length), comparator);
        return merge(leftArray, rightArray, comparator);
    }


    private static <T> T[] merge(T[] left, T[] right, Comparator comparator) {
        Object[] mergeArray = new Object[left.length + right.length];
        for (int index = 0, leftIndex = 0, rightIndex = 0; index < mergeArray.length; index++) {
            if (leftIndex >= left.length) {
                mergeArray[index] = right[rightIndex++];
            } else if (rightIndex >= right.length) {
                mergeArray[index] = left[leftIndex++];
            } else if (comparator.compare(left[leftIndex], right[rightIndex]) > 0) {
                mergeArray[index] = right[rightIndex++];
            } else {
                mergeArray[index] = left[leftIndex++];
            }
        }
        return (T[]) mergeArray;
    }


}
