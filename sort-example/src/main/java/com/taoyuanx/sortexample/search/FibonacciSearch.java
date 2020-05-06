package com.taoyuanx.sortexample.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author dushitaoyuan
 * @date 2020/5/6
 * 斐波那契 查找(有序查找 黄金分割比例 0.618)
 * <p>
 * 斐波那契数列满足:
 * f(n)=f(n-1)+f(n-2)
 * f(0)=1
 * f(1)=1
 * 也是二分查找的一种提升算法，通过运用黄金比例的概念在数列中选择查找点进行查找，提高查找效率。同样地，斐波那契查找也属于一种有序查找算法。
 * 　　相对于折半查找，一般将待比较的key值与第mid=（low+high）/2位置的元素比较，比较结果分三种情况：
 * 　　1）相等，mid位置的元素即为所求
 * 　　2）>，low=mid+1;
 * <p>
 * 3）<，high=mid-1。
 * <p>
 * 　　斐波那契查找与折半查找很相似，他是根据斐波那契序列的特点对有序表进行分割的。他要求开始表中记录的个数为某个斐波那契数小1，及n=F(k)-1;
 * <p>
 * 开始将k值与第F(k-1)位置的记录进行比较(及mid=low+F(k-1)-1),比较结果也分为三种
 * <p>
 * 　　1）相等，mid位置的元素即为所求
 * <p>
 * 　　2）>，low=mid+1,k-=2;
 * <p>
 * 　　说明：low=mid+1说明待查找的元素在[mid+1,high]范围内，k-=2 说明范围[mid+1,high]内的元素个数为n-(F(k-1))= Fk-1-F(k-1)=Fk-F(k-1)-1=F(k-2)-1个，所以可以递归的应用斐波那契查找。
 * <p>
 * 　　3）<，high=mid-1,k-=1。
 * <p>
 * 　　说明：low=mid+1说明待查找的元素在[low,mid-1]范围内，k-=1 说明范围[low,mid-1]内的元素个数为F(k-1)-1个，所以可以递归 的应用斐波那契查找。
 * <p>
 * 　　复杂度分析：最坏情况下，时间复杂度为O(log2n)，且其期望复杂度也为O(log2n)
 */
public class FibonacciSearch implements Search<Integer> {

    @Override
    public int search(Integer[] sortedData, Integer findData) {
        if (findData < sortedData[0] || findData > sortedData[sortedData.length - 1]) {
            return NOT_FIND;
        }
        List<Integer> tempList = new ArrayList<>(sortedData.length);
        int k = fibonacci(sortedData.length, tempList);
        Integer[] fibonacci = tempList.toArray(new Integer[tempList.size()]);
        Integer[] temp = Arrays.copyOf(sortedData, fibonacci[k] - 1);
        int low = 0, high = sortedData.length - 1;
        while (low <= high) {
            int middle = low + fibonacci[k - 1] - 1;
            Integer middleData = temp[middle] == null ? sortedData[sortedData.length - 1] : temp[middle];
            if (middleData.equals(findData)) {
                return middle;
            } else if (findData > middleData) {
                k -= 2;
                low = middle + 1;
            } else if (findData < middleData) {
                high = middle - 1;
                k -= 1;
            }
        }
        return NOT_FIND;
    }

    /**
     * 构造一个 斐波那契数列
     */
    public int fibonacci(int fibonacciSize, List<Integer> f) {
        f.add(1);
        f.add(2);
        int k = 0;
        for (int i = 2; fibonacciSize > f.get(k) - 1; i++, k++) {
            f.add(f.get(i - 1) + f.get(i - 2));
        }
        return k;

    }


}