package com.taoyuanx.sortexample.sort;

/**
 * @author dushitaoyuan
 * @date 2020/4/7
 */
public enum  SortEnum {

    BUBBLE("BubbleSort", "冒泡排序");
    public String sortName;
    public String desc;

    SortEnum(String sortName, String desc) {
        this.sortName = sortName;
        this.desc = desc;
    }

}
