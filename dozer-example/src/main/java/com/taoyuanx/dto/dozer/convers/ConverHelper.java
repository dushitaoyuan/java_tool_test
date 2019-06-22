package com.taoyuanx.dto.dozer.convers;

import java.util.regex.Pattern;

/**
 * @author 桃源
 * @description 转换帮助类
 * @date 2019/6/22
 */
public class ConverHelper {
    private  static  final Pattern YYYT_MM_DD_HH_MM_SS_P=Pattern.compile("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}");
    private  static  final String YYYT_MM_DD_HH_MM_SS="yyyy-MM-dd HH:mm:ss";


    private  static  final Pattern YYYT_MM_DD_P=Pattern.compile("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}");
    private  static  final String YYYT_MM_DD="yyyy-MM-dd HH:mm:ss";

    public static  String guessDateFormat(String date){
        if(YYYT_MM_DD_HH_MM_SS_P.matcher(date).matches()){
            return YYYT_MM_DD_HH_MM_SS;
        }
        if(YYYT_MM_DD_P.matcher(date).matches()){
            return YYYT_MM_DD;
        }
        return null;
    }
}
