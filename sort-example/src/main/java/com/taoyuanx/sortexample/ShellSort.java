package com.taoyuanx.sortexample;

import com.taoyuanx.sortexample.sort.Sort;

import java.util.Comparator;

/**
 * @author dushitaoyuan
 * @date 2020/4/7
 * @desc 希尔排序
 * 希尔排序是把记录按下表的一定增量分组，对每组使用直接插入排序算法排序；随着增量逐渐减少，每组包含的关键词越来越多，当增量减至1时，整个文件恰被分成一组，算法便终止。
 * <p>
 * 4.1 算法描述
 * <p>
 * 选择增量gap=length/2，缩小增量继续以gap = gap/2的方式，这种增量选择我们可以用一个序列来表示，{n/2,(n/2)/2...1}，称为增量序列。希尔排序的增量序列的选择与证明是个数学难题，我们选择的这个增量序列是比较常用的，也是希尔建议的增量，称为希尔增量，但其实这个增量序列不是最优的。此处我们做示例使用希尔增量。
 * <p>
 * 先将整个待排序的记录序列分割成为若干子序列分别进行直接插入排序，具体算法描述：
 * <p>
 * 选择一个增量序列t1，t2，…，tk，其中ti>tj，tk=1；
 * 按增量序列个数k，对序列进行k 趟排序；
 * 每趟排序，根据对应的增量ti，将待排序列分割成若干长度为m 的子序列，分别对各子表进行直接插入排序。仅增量因子为1 时，整个序列作为一个表来处理，表长度即为整个序列的长度。
 */
public class ShellSort<T> implements Sort<T> {

    @Override
    public T[] sort(T[] array, Comparator comparator) {
        /**
         * calcGap 为计算增量
         */
        int calcGap = array.length / 2, calcIndex;
        T temp = null;

        for (; calcGap > 0; calcGap /= 2) {
            /**
             * 插入排序,下标相差为 calcGap(缩小增量)的数为一个子数组
             */
            for (int index = calcGap; index < array.length; index++) {
                temp = array[index];
                calcIndex = index - calcGap;
                while (calcIndex >= 0 && comparator.compare(array[calcIndex], temp) > 0) {
                    array[calcIndex + calcGap] = array[calcIndex];
                    calcIndex -= calcGap;
                }
                array[calcIndex + calcGap] = temp;
            }
        }

        return array;
    }
}
