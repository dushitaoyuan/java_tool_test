package com.taoyuanx.guice.example.busi;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.taoyuanx.guice.example.service.CarService;
import com.taoyuanx.guice.example.service.EatService;
import com.taoyuanx.guice.example.service.impl.ComponentUtil;

/**
 * @author dushitaoyuan
 * @date 2019/12/16
 */
@Singleton
public class PeopleBusiness {
    @Inject
    CarService carService;
    @Inject(optional = true)
    EatService eatService;
    @Inject
    ComponentUtil componentUtil;

    public void dirve() {
        carService.dirve();
    }

    public void eat() {
        eatService.eat();

    }
    public void say() {
        componentUtil.say();

    }
}
