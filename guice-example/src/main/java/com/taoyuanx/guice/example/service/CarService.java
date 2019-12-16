package com.taoyuanx.guice.example.service;

import com.google.inject.ImplementedBy;
import com.taoyuanx.guice.example.service.impl.TruckCarServiceImpl;

/**
 * @author dushitaoyuan
 * @date 2019/12/16
 */
//@ImplementedBy(TruckCarServiceImpl.class)
public interface CarService   {
    void dirve();
}
