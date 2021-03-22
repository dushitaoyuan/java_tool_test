package com.taoyuanx.demo;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberExecutorScheduler;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.SuspendableRunnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dushitaoyuan
 * @desc 用途描述
 * @date 2021/2/7
 */
public class Demo {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        ExecutorService fiberPool = Executors.newFixedThreadPool(2);
        int num = 100;
        threadStart(threadPool, num);
        fiberStart(fiberPool, num);

    }

    public static void threadStart(ExecutorService executorService, int num) throws InterruptedException {
        long start = System.currentTimeMillis();
        AtomicInteger exeCount = new AtomicInteger();
        for (int i = 0; i < num; i++) {
            executorService.execute(() -> {
                try {
                    Thread.sleep(100);
                    exeCount.incrementAndGet();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
        while (!executorService.awaitTermination(1,TimeUnit.SECONDS)) {
        }
        long end = System.currentTimeMillis();
        System.out.println("执行数" + exeCount.get() + "\tthread all finished  \t" + (end - start));

    }

    public static void fiberStart(ExecutorService executor, int num) throws InterruptedException {
        FiberExecutorScheduler fiberExecutorScheduler = new FiberExecutorScheduler("deno", executor);
        long start = System.currentTimeMillis();
        AtomicInteger exeCount = new AtomicInteger();
        for (int i = 0; i < num; i++) {
            Fiber fiber = new Fiber(fiberExecutorScheduler, new SuspendableRunnable() {
                @Override
                public void run() throws SuspendExecution, InterruptedException {
                    try {
                        Thread.sleep(100);
                        exeCount.incrementAndGet();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            fiber.start();
        }
        executor.shutdown();
        while (!executor.awaitTermination(1,TimeUnit.SECONDS)) {
        }
        long end = System.currentTimeMillis();
        System.out.println("执行数" + exeCount.get() + "\t fiber all finished  \t" + (end - start));

    }


}
