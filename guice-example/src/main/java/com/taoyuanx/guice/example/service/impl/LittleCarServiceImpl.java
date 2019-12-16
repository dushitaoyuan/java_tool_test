package com.taoyuanx.guice.example.service.impl;

import com.taoyuanx.guice.example.anno.Primary;
import com.taoyuanx.guice.example.anno.Service;
import com.taoyuanx.guice.example.service.CarService;

/**
 * @author dushitaoyuan
 * @date 2019/12/16
 */
@Service
@Primary
public class LittleCarServiceImpl implements CarService {
    @Override
    public void dirve() {
        System.out.println("噗噗噗,开小汽车");
    }
}
