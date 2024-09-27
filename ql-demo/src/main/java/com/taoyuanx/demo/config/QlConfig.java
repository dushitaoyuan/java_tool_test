package com.taoyuanx.demo.config;

import com.ql.util.express.ExpressRunner;
import com.taoyuanx.demo.function.QlExpressUtil;
import com.taoyuanx.demo.function.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author lianglei78
 * @Date 2024/9/20 14:15
 */
@Configuration
public class QlConfig {
    @Bean
    public QlExpressUtil qlBean(@Autowired ApplicationContext applicationContext) throws Exception {
        SpringBeanUtil.init(applicationContext);
        ExpressRunner runner = new ExpressRunner();
        runner.addFunctionOfClassMethod("getBean", SpringBeanUtil.class.getName(), "getBean", new String[]{"String"}, "获取bean异常");
        QlExpressUtil.setApplicationContext(applicationContext);
        QlExpressUtil.setExpressRunner(runner);
        SpringBeanUtil.init(applicationContext);
        return new QlExpressUtil();
    }
}
