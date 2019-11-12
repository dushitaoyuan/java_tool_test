package com.taoyuanx.disruptor.example.event.factory;

import com.lmax.disruptor.EventFactory;
import com.taoyuanx.disruptor.example.event.LogEvent;

/**
 * @author dushitaoyuan
 * @desc 用途描述
 * @date 2019/11/6
 */
public class LogEventFactory implements EventFactory<LogEvent> {
    @Override
    public LogEvent newInstance() {
        return new LogEvent();
    }
}
