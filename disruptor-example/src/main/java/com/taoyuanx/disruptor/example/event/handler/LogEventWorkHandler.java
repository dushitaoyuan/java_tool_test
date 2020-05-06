package com.taoyuanx.disruptor.example.event.handler;

import com.lmax.disruptor.WorkHandler;
import com.taoyuanx.disruptor.example.event.LogEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author dushitaoyuan
 * @desc 用途描述
 * @date 2019/11/12
 */
@Slf4j
public class LogEventWorkHandler implements WorkHandler<LogEvent> {
    @Override
    public void onEvent(LogEvent event) throws Exception {
        event.add();
        log.info("1处理日志:{}",event);
        TimeUnit.SECONDS.sleep(3);
    }
}
