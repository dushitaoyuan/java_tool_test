package com.taoyuanx.dozer.convers;

import org.dozer.DozerConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author 桃源
 * @description  LocalDateTime <->String 互转
 * @date 2019/6/22
 */
public class LocalDateTimeStringConverter extends DozerConverter<LocalDateTime, String> {
    public static  final String DEFAULT_FORMAT="yyyy-MM-dd HH:mm:ss.SSS";
    public LocalDateTimeStringConverter(Class<LocalDateTime> prototypeA, Class<String> prototypeB) {
        super(prototypeA, prototypeB);
    }
    public LocalDateTimeStringConverter() {
        super(LocalDateTime.class, String.class);
    }
    @Override
    public String convertTo(LocalDateTime source, String destination) {
        if(null!=source){
            return source.format(DateTimeFormatter.ofPattern(DEFAULT_FORMAT));
        }
        return null;
    }

    @Override
    public LocalDateTime convertFrom(String source, LocalDateTime destination) {
        if(null!=source&&source.length()>0){
            try {
                return LocalDateTime.parse(source,DateTimeFormatter.ofPattern(DEFAULT_FORMAT));
            }catch (Exception e){
                return LocalDateTime.parse(source,DateTimeFormatter.ofPattern(ConverHelper.guessDateFormat(source)));
            }

        }
        return null;
    }
}
