package com.taoyuanx.disruptor.example.event.handler;


import com.lmax.disruptor.EventHandler;
import com.taoyuanx.disruptor.example.event.LogEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogEventHandler implements EventHandler<LogEvent>
{



    @Override
    public void onEvent(LogEvent logEvent, long sequence, boolean endOfBatch) throws Exception {
        log.info("处理日志:{}",logEvent);
    }
}