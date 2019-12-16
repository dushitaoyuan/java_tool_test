package com.taoyuanx.guice.example.service.impl;

import com.google.inject.Singleton;
import com.taoyuanx.guice.example.anno.Component;

/**
 * @author dushitaoyuan
 * @date 2019/12/16
 */
@Component
@Singleton
public class ComponentUtil {
    public  void say(){
        System.out.println("i am component util");
    }
}
