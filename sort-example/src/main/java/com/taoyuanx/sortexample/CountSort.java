package com.taoyuanx.sortexample;

import com.taoyuanx.sortexample.sort.Sort;

import java.util.Comparator;

/**
 * @author dushitaoyuan
 * @date 2020/4/10
 * @desc 计数排序(只能对数值类型)
 * 找出待排序的数组中最大和最小的元素；
 * 统计数组中每个值为i的元素出现的次数，存入数组C的第i项；
 * 对所有的计数累加（从C中的第一个元素开始，每一项和前一项相加）；
 * 反向填充目标数组：将每个元素i放在新数组的第C(i)项，每放一个元素就将C(i)减去1。
 */
public class CountSort implements Sort<Integer> {

    @Override
    public Integer[] sort(Integer[] array, Comparator comparator) {
        Integer max = array[0], min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (comparator.compare(array[i], max) > 0) {
                max = array[i];
            }
            if (comparator.compare(min, array[i]) > 0) {
                min = array[i];
            }
        }

        Integer[] countArray = new Integer[Math.abs(max - min) + 1];
        for (int i = 0; i < array.length; i++) {
            /**
             * 计算数值所在位置
             */
            int countIndex = Math.abs(array[i] - min);
            if (countArray[countIndex] == null) {
                countArray[countIndex] = 1;
            } else {
                countArray[countIndex] += 1;
            }
        }
        int arrayIndex = 0;
        for (int i = 0; i < countArray.length; i++) {
            while (countArray[i] != null && countArray[i] > 0) {
                /**
                 * 根据 计数位置还原原始数
                 */
                int num = i + min;
                if (num > max) {
                    num = min - i;
                }
                array[arrayIndex] = num;
                countArray[i] -= 1;
                arrayIndex++;
            }
        }
        return array;
    }
}
