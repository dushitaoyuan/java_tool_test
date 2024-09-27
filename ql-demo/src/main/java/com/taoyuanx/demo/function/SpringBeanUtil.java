package com.taoyuanx.demo.function;


import org.springframework.context.ApplicationContext;

/**
 * @Author lianglei78
 * @Date 2024/9/20 14:10
 * @Description soring bean容器
 */
public class SpringBeanUtil {

    private static ApplicationContext applicationContext;

    public static void init(ApplicationContext applicationContext) {
        SpringBeanUtil.applicationContext = applicationContext;
    }

    public static <T> T getBean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }

}
