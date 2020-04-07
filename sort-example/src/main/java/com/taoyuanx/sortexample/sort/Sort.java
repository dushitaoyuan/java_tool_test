package com.taoyuanx.sortexample.sort;

import java.util.Comparator;

/**
 * @author dushitaoyuan
 * @date 2020/4/7
 */
public interface Sort<T> {
    T[] sort(T[] array, Comparator comparator);
}
