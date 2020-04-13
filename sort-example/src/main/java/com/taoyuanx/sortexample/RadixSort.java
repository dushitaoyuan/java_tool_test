package com.taoyuanx.sortexample;

import com.taoyuanx.sortexample.sort.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author dushitaoyuan
 * @date 2020/4/10
 * @desc 桶排序
 * 基数排序也是非比较的排序算法，对每一位进行排序，从最低位开始排序，复杂度为O(kn),为数组长度，k为数组中的数的最大的位数；
 * 基数排序是按照低位先排序，然后收集；再按照高位排序，然后再收集；依次类推，直到最高位。有时候有些属性是有优先级顺序的，先按低优先级排序，再按高优先级排序。
 * 最后的次序就是高优先级高的在前，高优先级相同的低优先级高的在前。基数排序基于分别排序，分别收集，所以是稳定的
 */
public class RadixSort implements Sort<Integer> {

    @Override
    public Integer[] sort(Integer[] array, Comparator comparator) {
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            max = Math.max(max, array[i]);
        }
        int digitNum = 0;
        while (max != 0) {
            max /= 10;
            digitNum++;
        }
        boolean desc = false;
        if (comparator.compare(max, array[0]) >= 0) {
            desc = true;
        }
        /**
         * 0-9  10个桶
         */
        int mod = 10, div = 1;
        ArrayList<ArrayList<Integer>> bucketList = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < 10; i++) {
            bucketList.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < digitNum; i++, mod *= 10, div *= 10) {
            for (int j = 0; j < array.length; j++) {
                int num = (array[j] % mod) / div;
                if (desc) {
                    num = 9 - num;
                }
                bucketList.get(num).add(array[j]);
            }
            int index = 0;
            for (int j = 0; j < bucketList.size(); j++) {
                for (int k = 0; k < bucketList.get(j).size(); k++) {
                    array[index++] = bucketList.get(j).get(k);
                }
                bucketList.get(j).clear();
            }
        }

        return array;
    }


}
