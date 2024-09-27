package com.taoyuanx.demo.service;

import org.springframework.stereotype.Component;

/**
 * @Author lianglei78
 * @Date 2024/9/20 14:33
 */
@Component
public class DemoService {
    public String hello(String hello){
        System.out.println("hello world");
        return hello+" 你好！";
    }
    public int add(int a,int b){
        return a+b;
    }
}
