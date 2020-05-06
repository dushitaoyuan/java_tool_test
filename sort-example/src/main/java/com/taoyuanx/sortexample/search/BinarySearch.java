package com.taoyuanx.sortexample.search;

/**
 * @author dushitaoyuan
 * @date 2020/5/6
 * 二分查找
 * <p>
 * 适合有序查找
 */
public class BinarySearch implements Search<Integer> {
    private int findCount = 0;

    @Override
    public int search(Integer[] sortedData, Integer findData) {
        if (findData < sortedData[0] || findData > sortedData[sortedData.length - 1]) {
            return NOT_FIND;
        }
        //return recursionSearch(sortedData, findData, 0, sortedData.length);
        return searchWhile(sortedData, findData);

    }

    public int recursionSearch(Integer[] sortedData, Integer findData, int low, int high) {
        int middle = low + (high - low) / 2;
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

    public int searchWhile(Integer[] sortedData, Integer findData) {
        int low = 0, high = sortedData.length - 1;
        while (low <= high) {
            findCount++;
            int middle = low + (high - low) / 2;
            Integer middleData = sortedData[middle];
            if (middleData.equals(findData)) {
                return middle;
            } else if (findData < middleData) {
                high = middle - 1;
            } else if (findData > middleData) {
                low = middle + 1;
            }
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
