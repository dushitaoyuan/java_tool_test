package com.taoyuanx.sortexample.search;

/**
 * 二叉树,红黑树,平衡二叉树JAVA中有标准实现
 */
public interface Search<T> {
    int NOT_FIND = -1;

    int search(T[] data, T findData);

}