package com.taoyuanx.demo;

import com.alibaba.fastjson2.JSON;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;
import org.junit.jupiter.api.Test;

/**
 * @Author lianglei78
 * @Date 2024/9/20 10:39
 */
public class QlSimpleTest {
    @Test
    public void simpleMathTest() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        context.put("a", 1);
        context.put("b", 2);
        context.put("c", 3);
        context.put("d", 1);
        String express = "a + b * c-d/a";
        execPrint(express, context,true);
    }

    @Test
    public void logicTest() throws Exception {
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        context.put("flagTrue", true);
        context.put("flagFalse", false);
        context.put("a", "4");
        execPrint("a>1", context,true);
        execPrint("a<10", context,false);
        execPrint("flagTrue && flagFalse", context,false);
        execPrint("flagTrue || flagFalse ? true:false", context,false);

        execPrint("flagTrue && a<3", context,false);
        execPrint("a!=4", context,false);
        execPrint("a==\"4\"", context,false);
        execPrint("a==4", context,false);
        execPrint("a.equals(\"4\")", context,false);




    }

    public static void print(String express, Object result, IExpressContext<String, Object> context, boolean withContext) {
        if (withContext) {
            System.out.println("context = " + JSON.toJSONString(context));
        }
        System.out.println(express + " = " + JSON.toJSONString(result));
        System.out.println("--------------------------------------------------------------");

    }

    public void execPrint(String express, IExpressContext<String, Object> context, boolean withContext) throws Exception {
        ExpressRunner runner = new ExpressRunner();
        Object r = runner.execute(express, context, null, true, false);
        print(express, r, context, withContext);
    }

}
