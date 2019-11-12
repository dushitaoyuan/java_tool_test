package com.taoyuanx.disruptor.example.event.handler;


import com.lmax.disruptor.EventHandler;
import com.taoyuanx.disruptor.example.event.Clearable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClearEventHandler implements EventHandler<Clearable> {


    @Override
    public void onEvent(Clearable clearEvent, long sequence, boolean endOfBatch) throws Exception {
        clearEvent.clear();
        log.info("对象清理,{}",clearEvent);
    }
}