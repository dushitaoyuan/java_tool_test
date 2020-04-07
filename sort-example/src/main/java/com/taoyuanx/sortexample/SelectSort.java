package com.taoyuanx.sortexample;

import com.taoyuanx.sortexample.sort.Sort;

import java.util.Comparator;

/**
 * @author dushitaoyuan
 * @date 2020/4/7
 * @desc 选择排序
 */
public class SelectSort<T> implements Sort<T> {

    @Override
    public T[] sort(T[] array, Comparator comparator) {
        T temp;
        int selectIndex = 0;
        for (int i = 0; i < array.length; i++) {
            selectIndex = i;
            for (int j = i; j < array.length; j++) {
                if (comparator.compare(array[selectIndex], array[j]) > 0) {
                    selectIndex = j;
                }
            }
            if (selectIndex != i) {
                //swap
                temp = array[selectIndex];
                array[selectIndex] = array[i];
                array[i] = temp;

            }
        }
        return array;
    }
}
