package com.taoyuanx.sortexample;

import com.taoyuanx.sortexample.sort.Sort;

import java.util.Comparator;

/**
 * @author dushitaoyuan
 * @date 2020/4/7
 * @desc 插入排序
 *
 * 从第一个元素开始，该元素可以认为已经被排序；
 * 取出下一个元素，在已经排序的元素序列中从后向前扫描；
 * 如果该元素（已排序）大于新元素，将该元素移到下一位置；
 * 重复步骤3，直到找到已排序的元素小于或者等于新元素的位置；
 * 将新元素插入到该位置后；
 * 重复步骤2~5。
 */
public class InsertSort<T> implements Sort<T> {

    @Override
    public T[] sort(T[] array, Comparator comparator) {
        for (int i = 0; i < array.length - 1; i++) {
            T current = array[i + 1];
            int sortIndex = i;
            while (sortIndex >= 0 && comparator.compare(array[sortIndex], current) > 0) {
                //后移一位
                array[sortIndex + 1] = array[sortIndex];
                sortIndex--;
            }
            //插入
            array[sortIndex + 1] = current;

        }

        return array;
    }
}
