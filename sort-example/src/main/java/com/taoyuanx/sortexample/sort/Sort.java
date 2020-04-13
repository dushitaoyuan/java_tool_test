package com.taoyuanx.sortexample.sort;

import java.util.Comparator;

/**
 * @author dushitaoyuan
 * @date 2020/4/7
 * 参考地址:https://blog.csdn.net/meibenxiang/article/details/92796909
 */
public interface Sort<T> {
    T[] sort(T[] array, Comparator comparator);
}
