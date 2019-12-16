package com.taoyuanx.guice.example.service.impl;

import com.taoyuanx.guice.example.anno.Service;
import com.taoyuanx.guice.example.service.CarService;

/**
 * @author dushitaoyuan
 * @desc 用途描述
 * @date 2019/12/16
 */
@Service
public class TruckCarServiceImpl implements CarService {
    @Override
    public void dirve() {
        System.out.println("嘟嘟嘟,开货车");
    }
}
