package com.taoyuanx.disruptor.example.event;

import lombok.Data;

import java.util.Objects;

/**
 * @author dushitaoyuan
 * @date 2019/11/6
 */
@Data
public class LogEvent implements Clearable {
    private String logDetail;
    private Long count;

    //对象复用清理
    @Override
    public void clear() {
        this.logDetail = null;
        this.count = null;
    }

    public void add() {
        if (Objects.isNull(count)) {
            count = 1L;
        } else {
            count++;
        }
    }
}
