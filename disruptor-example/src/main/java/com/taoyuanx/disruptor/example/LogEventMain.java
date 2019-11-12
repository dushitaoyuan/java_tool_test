package com.taoyuanx.disruptor.example;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.taoyuanx.disruptor.example.event.LogEvent;
import com.taoyuanx.disruptor.example.event.factory.LogEventFactory;
import com.taoyuanx.disruptor.example.event.handler.ClearEventHandler;
import com.taoyuanx.disruptor.example.event.handler.LogEventHandler;
import com.taoyuanx.disruptor.example.event.handler.LogEventWorkHandler;
import com.taoyuanx.disruptor.example.event.handler.LogEventWorkHandler2;
import com.taoyuanx.disruptor.example.producer.LogEventProducer;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class LogEventMain {
    public static void main(String[] args) throws Exception {
        mulConsumer();
    }
    public static void mulConsumer() {
        int bufferSize = 1024, consumerSize = 10;
        LogEventFactory logEventFactory = new LogEventFactory();
        Disruptor<LogEvent> disruptor = new Disruptor<>(logEventFactory, bufferSize, DaemonThreadFactory.INSTANCE, ProducerType.MULTI, new BlockingWaitStrategy());
        LogEventWorkHandler[] workPool = new LogEventWorkHandler[consumerSize];
        for (int i = 0; i < consumerSize; i++) {
            workPool[i] = new LogEventWorkHandler();
        }

        LogEventWorkHandler2[] workPool2 = new LogEventWorkHandler2[consumerSize];
        for (int i = 0; i < consumerSize; i++) {
            workPool2[i] = new LogEventWorkHandler2();
        }
        //一个事件可以被多个handler处理 发布订阅模式
        //一个事件被相同handler互斥处理 队列模式
        //链式调用可以保证事件像流水线上的产品(事件)一样被线工(事件处理)操作
        disruptor.handleEventsWithWorkerPool(workPool).handleEventsWithWorkerPool(workPool2).then(new ClearEventHandler());
        disruptor.start();

        RingBuffer<LogEvent> ringBuffer = disruptor.getRingBuffer();
        LogEventProducer logEventProducer = new LogEventProducer(ringBuffer);
        int batch = 10;
        ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        for (int index = 0; index < batch; index++) {
            int finalIndex = index;
            poolExecutor.submit(() -> {
                logEventProducer.producerData(finalIndex);
            });
        }

        disruptor.shutdown();
    }

    public static void singleConsumer() {
        int bufferSize = 1024;
        LogEventFactory logEventFactory = new LogEventFactory();
        Disruptor<LogEvent> disruptor = new Disruptor<>(logEventFactory, bufferSize, DaemonThreadFactory.INSTANCE, ProducerType.SINGLE, new BlockingWaitStrategy());

        disruptor.handleEventsWith(new LogEventHandler()).then(new ClearEventHandler());
        disruptor.start();

        RingBuffer<LogEvent> ringBuffer = disruptor.getRingBuffer();
        LogEventProducer logEventProducer = new LogEventProducer(ringBuffer);
        long batch = 100;
        for (long index = 0; index < batch; index++) {
            logEventProducer.producerData(index);
        }
        disruptor.shutdown();
    }
}