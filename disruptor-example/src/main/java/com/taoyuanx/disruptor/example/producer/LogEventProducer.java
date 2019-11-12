package com.taoyuanx.disruptor.example.producer;


import com.lmax.disruptor.RingBuffer;
import com.taoyuanx.disruptor.example.event.LogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogEventProducer {
    private final RingBuffer<LogEvent> ringBuffer;
    private static Logger log = LoggerFactory.getLogger(LogEventProducer.class);

    public LogEventProducer(RingBuffer<LogEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void producerData(long index) {
        long sequence = ringBuffer.next();
        try {
            LogEvent event = ringBuffer.get(sequence);
            event.setLogDetail(Thread.currentThread().getName() + "\t日志:\t" + index);
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}