package com.taoyuanx.sortexample.search;

/**
 * @author dushitaoyuan
 * @date 2020/5/6
 * 顺序查找
 */
public class SequenceSearch implements Search<Integer> {
    @Override
    public int search(Integer[] data, Integer findData) {
        for (int index = 0; index < data.length; index++) {
            if (data[index].equals(findData)) {
                return index;
            }
        }
        return NOT_FIND;
    }


}
