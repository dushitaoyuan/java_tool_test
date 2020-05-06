package com.taoyuanx.sortexample.search;

/**
 * @author dushitaoyuan
 * @date 2020/5/6
 * 差值查找（二分查找的优化，适合有序查找，分布较为均匀）
 */
public class DValueSearch implements Search<Integer> {
    private int findCount = 0;

    @Override
    public int search(Integer[] data, Integer findData) {
        return recursionSearch(data, findData, 0, data.length - 1);
    }

    public int recursionSearch(Integer[] sortedData, Integer findData, int low, int high) {
        if(findData<sortedData[low]||findData>sortedData[high]){
            return NOT_FIND;
        }
        int middle = low + (findData - sortedData[low]) * (high - low) / (sortedData[high] - sortedData[low]);
        findCount++;
        if (sortedData[middle].equals(findData)) {
            return middle;
        } else if (findData > sortedData[middle]) {
            return recursionSearch(sortedData, findData, middle + 1, high);
        } else if (findData < sortedData[middle]) {
            return recursionSearch(sortedData, findData, low, middle - 1);
        }
        return NOT_FIND;
    }

    public void sayStat() {
        System.out.println("findcount " + findCount);
    }

    public void restStat() {
        findCount = 0;
    }
}
