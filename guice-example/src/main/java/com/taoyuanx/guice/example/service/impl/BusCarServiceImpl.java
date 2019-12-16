package com.taoyuanx.guice.example.service.impl;

import com.taoyuanx.guice.example.anno.Service;
import com.taoyuanx.guice.example.service.CarService;

/**
 * @author dushitaoyuan
 * @date 2019/12/16
 */
@Service
public class BusCarServiceImpl implements CarService {
    @Override
    public void dirve() {
        System.out.println("滴滴滴,开bus");
    }
}
