package com.taoyuanx.demo;

import com.ql.util.express.DefaultContext;
import com.taoyuanx.demo.function.QlExpressUtil;
import org.junit.jupiter.api.Test;


/**
 * @Author lianglei78
 * @Date 2024/9/20 14:30
 */
public class QlSpringTest extends BaseTest {

    @Test
    public void callSpringBeanMethodTest() throws Exception {
        String express = "demoService.hello(\"hello world\")";
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        Object result = QlExpressUtil.execute(express, context);
        QlSimpleTest.print(express, result, context, true);
        result = QlExpressUtil.execute("getBean(\"demoService\").hello(\"hello world\")", context);
        QlSimpleTest.print(express, result, context, true);

        express = "demoService.add(1,2)";
        result = QlExpressUtil.execute(express, context);
        QlSimpleTest.print(express, result, context, true);


    }
}
