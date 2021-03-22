package com.taoyuanx.sortexample;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author dushitaoyuan
 * @date 2021/3/22
 */
public class DemoTest {
    public static void main(String[] args) {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(3, 6,
                10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100));
        poolExecutor.submit(()->{
            System.out.println("1");
        });
    }
}
