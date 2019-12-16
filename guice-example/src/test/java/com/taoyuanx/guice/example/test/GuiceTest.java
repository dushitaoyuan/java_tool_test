
package com.taoyuanx.guice.example.test;

import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.taoyuanx.guice.example.busi.PeopleBusiness;
import com.taoyuanx.guice.example.service.CarService;
import com.taoyuanx.guice.example.service.impl.ComponentUtil;
import com.taoyuanx.guice.example.test.module.BasePackageModule;
import com.taoyuanx.guice.example.test.module.PeopleModule;
import org.junit.Test;

import java.util.Map;

/**
 * @author dushitaoyuan
 * @date 2019/12/16
 */
public class GuiceTest {
    @Test
    public  void guiceImplByTest(){
        Injector injector = Guice.createInjector();
        PeopleBusiness peopleBusiness = injector.getInstance(PeopleBusiness.class);
        peopleBusiness.dirve();

    }

    @Test
    public  void guiceModuleTest(){
        Injector injector = Guice.createInjector(new PeopleModule());
        PeopleBusiness peopleBusiness = injector.getInstance(PeopleBusiness.class);
        peopleBusiness.dirve();
        peopleBusiness.eat();

    }

    /**
     * 简单实现spring形式依赖注入
     */
    @Test
    public  void guiceBasePackageModuleTest(){
        Injector injector = Guice.createInjector(new BasePackageModule("com.taoyuanx.guice.example.service"));
        PeopleBusiness peopleBusiness = injector.getInstance(PeopleBusiness.class);
        peopleBusiness.dirve();
        peopleBusiness.eat();
        peopleBusiness.say();


    }
}
