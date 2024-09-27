package com.taoyuanx.demo.function;

import java.util.Map;

import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class QlExpressUtil {
    private static ExpressRunner runner;
    private static ApplicationContext applicationContext;


    /**
     * @param statement 执行语句
     * @param context   上下文
     * @throws Exception
     */
    public static Object execute(String statement, Map<String, Object> context) throws Exception {
        IExpressContext expressContext = new QLExpressContext(context, applicationContext);
        return runner.execute(statement, expressContext, null, true, false);
    }

    public static void setExpressRunner(ExpressRunner runner) throws BeansException {
        QlExpressUtil.runner = runner;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        QlExpressUtil.applicationContext = applicationContext;
    }
}