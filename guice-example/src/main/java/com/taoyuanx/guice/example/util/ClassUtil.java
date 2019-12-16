package com.taoyuanx.guice.example.util;

/**
 * @author dushitaoyuan
 * @date 2019/12/16
 */
public class ClassUtil {
    public  static  String firstLower(String str){
        String first=str.substring(0,1);
        return  str.replaceFirst(first,first.toLowerCase());
    }
    public  static  boolean isEmpty(String str){
        return  str==null||str.length()==0;
    }
}
