package com.taoyuanx.dozer.convers;


import com.github.dozermapper.core.DozerConverter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author 桃源
 * @description LocalDateTime <->Date 互转
 * @date 2019/6/22
 */
public class LocalDateTimeDateConverter extends DozerConverter<LocalDateTime, Date> {
    public LocalDateTimeDateConverter(Class<LocalDateTime> prototypeA, Class<Date> prototypeB) {
        super(prototypeA, prototypeB);
    }
    public LocalDateTimeDateConverter() {
        super(LocalDateTime.class, Date.class);
    }

    @Override
    public Date convertTo(LocalDateTime source, Date destination) {
        if(null!=source){
            return Date.from(source.atZone(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }

    @Override
    public LocalDateTime convertFrom(Date source, LocalDateTime destination) {
        if(null!=source){
            return LocalDateTime.ofInstant(source.toInstant(),ZoneId.systemDefault());
        }
        return null;
    }
}
