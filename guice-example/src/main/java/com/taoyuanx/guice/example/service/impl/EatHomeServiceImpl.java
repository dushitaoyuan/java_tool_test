package com.taoyuanx.guice.example.service.impl;

import com.taoyuanx.guice.example.anno.Component;
import com.taoyuanx.guice.example.anno.Service;
import com.taoyuanx.guice.example.service.EatService;

/**
 * @author dushitaoyuan
 * @date 2019/12/16
 */
@Component
public class EatHomeServiceImpl implements EatService {
    @Override
    public void eat() {
        System.out.println("在家吃饭");
    }
}
