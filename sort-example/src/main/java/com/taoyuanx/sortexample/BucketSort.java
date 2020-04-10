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
 * 桶排序是计数排序的升级版。它利用了函数的映射关系，高效与否的关键就在于这个映射函数的确定。
 * <p>
 * 桶排序 (Bucket sort)的工作的原理：假设输入数据服从均匀分布，将数据分到有限数量的桶里，每个桶再分别排序（有可能再使用别的排序算法或是以递归方式继续使用桶排序进行排
 * 人为设置一个BucketSize，作为每个桶所能放置多少个不同数值（例如当BucketSize==5时，该桶可以存放｛1,2,3,4,5｝这几种数字，但是容量不限，即可以存放100个3）；
 * 遍历输入数据，并且把数据一个一个放到对应的桶里去；
 * 对每个不是空的桶进行排序，可以使用其它排序方法，也可以递归使用桶排序；
 * 从不是空的桶里把排好序的数据拼接起来。 
 * 注意，如果递归使用桶排序为各个桶排序，则当桶数量为1时要手动减小BucketSize增加下一循环桶的数量，否则会陷入死循环，导致内存溢出
 */
public class BucketSort implements Sort<Integer> {

    private Integer bucketSize;

    public BucketSort(Integer bucketSize) {
        this.bucketSize = bucketSize;
    }

    @Override
    public Integer[] sort(Integer[] array, Comparator comparator) {
        return (Integer[]) bucketSort(Arrays.asList(array), comparator, bucketSize).toArray();
    }

    private List<Integer> bucketSort(List<Integer> array, Comparator comparator, int bucketSize) {
        Integer max = array.get(0), min = max;
        for (int i = 1, len = array.size(); i < len; i++) {
            if (comparator.compare(array.get(i), max) > 0) {
                max = array.get(i);
            }
            if (comparator.compare(min, array.get(i)) > 0) {
                min = array.get(i);
            }
        }
        Integer bucketCount = Math.abs(max - min) / bucketSize;
        List<List<Integer>> bucketList = new ArrayList<>();
        for (int i = 0;i < bucketCount; i++) {
            bucketList.add(new ArrayList<>());
        }
        List<Integer> result = new ArrayList<>();
        for (int i = 0, len = array.size(); i < len; i++) {
            int bucketNum = (array.get(i) - min) / bucketSize;
            bucketList.get(bucketNum).add( array.get(i));
        }
        for (int i = 0; i < bucketCount; i++) {
            if (bucketSize == 1) {
                for (int j = 0, len = bucketList.get(i).size(); j < len; j++) {
                    result.add(bucketList.get(i).get(j));
                }
            } else {
                if (bucketCount == 1) {
                    bucketSize--;
                }
                List<Integer> bucketSort = bucketSort(bucketList.get(i), comparator, bucketSize);
                for (int j = 0; j < bucketSort.size(); j++) {
                    result.add(bucketSort.get(j));
                }
            }
        }
        return result;
    }


}
