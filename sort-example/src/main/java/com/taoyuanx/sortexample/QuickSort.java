package com.taoyuanx.sortexample;

import com.taoyuanx.sortexample.sort.Sort;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author dushitaoyuan
 * @date 2020/4/7
 * @desc 归并排序
 * 从数列中挑出一个元素，称为 “基准”（pivot）；
 * 重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面（相同的数可以到任一边）。在这个分区退出之后，该基准就处于数列的中间位置。这个称为分区（partition）操作；
 * 递归地（recursive）把小于基准值元素的子数列和大于基准值元素的子数列排序
 */
public class QuickSort<T> implements Sort<T> {


    @Override
    public T[] sort(T[] array, Comparator comparator) {
        quickSort(array, 0, array.length - 1, comparator);
        return array;
    }

    private void quickSort(T[] array, int low, int high, Comparator comparator) {
        if (low < high) {
            int pivotIndex = partion(array, low, high, comparator);
            quickSort(array, low, pivotIndex - 1, comparator);
            quickSort(array, pivotIndex + 1, high, comparator);
        }
    }

    private int partion(T[] array, int low, int high, Comparator comparator) {
        T pivot = array[low];
        while (low < high) {
            //找高部分比基准数大的下标
            while (low < high && comparator.compare(array[high], pivot) >=0) {
                high--;
            }
            array[low] = array[high];
            //找低部分比基准数小的下标
            while (low < high && comparator.compare(pivot, array[low]) >= 0) {
                low++;
            }
            array[high] = array[low];

        }
        array[low] = pivot;
        return low;
    }


}
