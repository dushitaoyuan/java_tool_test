package com.taoyuanx.guice.example.test.module;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.ClassScaner;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.taoyuanx.guice.example.anno.Component;
import com.taoyuanx.guice.example.anno.Primary;
import com.taoyuanx.guice.example.anno.Service;
import com.taoyuanx.guice.example.service.impl.TruckCarServiceImpl;
import com.taoyuanx.guice.example.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;
import sun.reflect.generics.scope.ConstructorScope;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dushitaoyuan
 * @desc guice 自动扫描
 * @date 2019/12/16
 */
@Slf4j
public class BasePackageModule extends AbstractModule {

    private List<String> basePackageList;

    public BasePackageModule(String... basePackages) {
        this.basePackageList = Arrays.asList(basePackages);
    }

    @Override
    protected void configure() {
        Set<Class> allClassSet = scanClass();
        //key class:value 实现类或自身class, 分组
        Map<Class, List<Class>> serviceImplMap = allClassSet.stream().filter((Class clazz)->{
            return  clazz.isAnnotationPresent(Service.class)||clazz.isAnnotationPresent(Component.class);
        }).collect(Collectors.groupingBy((Class clazz) -> getInterfaceClass(clazz)));
        /**
         * 注入guice容器
         * 1.有实现 注入所有实现
         * 2.没有实现注入自身
         * 3.为支持 @Name 类似@Qualifier ,需保证 直接 使用 @Inject 可以找到primary依赖,如无primary依赖,注入实现列表的第一个
         */
        for (Class interfaceClass : serviceImplMap.keySet()) {
            List<Class> implList = serviceImplMap.get(interfaceClass);
            if(!interfaceClass.isInterface()){
                log.info("注入  {}",interfaceClass);
                try {
                    bind(interfaceClass).toConstructor(interfaceClass.getConstructor());
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                continue;
            }
            if(CollectionUtil.isEmpty(implList)){
                log.info("注入  {}",interfaceClass);
                try {
                    bind(interfaceClass).toConstructor(interfaceClass.getConstructor());
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                continue;
            }else {
                boolean hasPrimary=false;
                for(Class impl:implList){
                    if(!hasPrimary && impl.isAnnotationPresent(Primary.class)){
                        log.debug("注入 primary  {}, impl : {} ",interfaceClass,impl);
                        bind(interfaceClass).to(Key.get(impl));
                        hasPrimary=true;
                    }else {
                        String beanName=beanName(impl);
                        log.debug("注入 {},name:{}, impl : {} ",interfaceClass,beanName,impl);
                        bind(interfaceClass).annotatedWith(Names.named(beanName)).to(impl);
                    }
                }
                if(!hasPrimary){
                    Class primaryImpl = implList.get(0);
                    log.debug("注入 primary  {}, impl : {} ",interfaceClass,primaryImpl);
                    try {
                        bind(interfaceClass).toConstructor(primaryImpl.getConstructor());
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private Set<Class> scanClass() {
        Set<Class> allClass = new HashSet<>();
        if (basePackageList == null || basePackageList.isEmpty()) {
            allClass.addAll( ClassScaner.scanPackage());
            return allClass;
        }else {
            for (String basePackage : basePackageList) {
                Set<Class<?>> classSet = ClassScaner.scanPackage(basePackage);
                if (classSet != null && !classSet.isEmpty()) {
                    allClass.addAll(classSet);
                }
            }
        }
        return allClass;
    }

    private Class getInterfaceClass(Class clazz) {
        if (clazz.getInterfaces().length >= 1) {
            return clazz.getInterfaces()[0];
        }
        return clazz;
    }



    public String beanName(Class clazz) {
        try {
            String beanName = ClassUtil.firstLower(clazz.getSimpleName());
            if (clazz.isAnnotationPresent(Service.class)) {
                Service service = (Service) clazz.getAnnotation(Service.class);
                return ClassUtil.isEmpty(service.value()) ? beanName : service.value();
            }
            if (clazz.isAnnotationPresent(Component.class)) {
                Component component = (Component) clazz.getAnnotation(Component.class);
                return ClassUtil.isEmpty(component.value()) ? beanName : component.value();
            }
            return beanName;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
