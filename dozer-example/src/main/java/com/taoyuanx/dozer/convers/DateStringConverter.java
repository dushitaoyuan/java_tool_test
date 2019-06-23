package com.taoyuanx.dozer.convers;

import com.vip.vjtools.vjkit.time.DateFormatUtil;
import org.dozer.DozerConverter;

import java.text.ParseException;
import java.util.Date;

/**
 * @author 桃源
 * @description date <->string 互转
 * @date 2019/6/22
 */
public class DateStringConverter extends DozerConverter<Date, String> {
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    public DateStringConverter(Class<Date> prototypeA, Class<String> prototypeB) {
        super(prototypeA, prototypeB);
    }

    public DateStringConverter() {
        super(Date.class, String.class);
    }

    @Override
    public String convertTo(Date source, String destination) {
        if (null != source) {
            return DateFormatUtil.formatDate(DEFAULT_FORMAT, source);
        }
        return null;
    }

    @Override
    public Date convertFrom(String source, Date destination) {

        try {
            if (null != source && source.length() > 0) {
                try {
                    return DateFormatUtil.parseDate(DEFAULT_FORMAT, source);
                } catch (ParseException e) {
                    return DateFormatUtil.parseDate(ConverHelper.guessDateFormat(source), source);
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
