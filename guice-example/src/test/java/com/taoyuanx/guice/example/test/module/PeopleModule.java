package com.taoyuanx.guice.example.test.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.taoyuanx.guice.example.service.CarService;
import com.taoyuanx.guice.example.service.EatService;
import com.taoyuanx.guice.example.service.impl.BusCarServiceImpl;
import com.taoyuanx.guice.example.service.impl.EatHomeServiceImpl;
import com.taoyuanx.guice.example.service.impl.TruckCarServiceImpl;

/**
 * @author dushitaoyuan
 * @desc guice module
 * @date 2019/12/16
 */
public class PeopleModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(CarService.class).annotatedWith(Names.named("truck")).to(TruckCarServiceImpl.class);
        bind(CarService.class).annotatedWith(Names.named("bus")).to(BusCarServiceImpl.class);
        bind(CarService.class).annotatedWith(Names.named("littleCar")).to(BusCarServiceImpl.class);
        bind(EatService.class).to(EatHomeServiceImpl.class);
    }

    @Provides
    public CarService carService(@Named("truck") CarService truck, @Named("bus") CarService bus) {
       if(  Math.random()>0.5){
           return truck;
       }
       return  bus;

    }

}
