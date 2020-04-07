package com.taoyuanx.sortexample;

import com.taoyuanx.sortexample.sort.Sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author dushitaoyuan
 * @date 2020/4/7
 * @desc 插入排序
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
