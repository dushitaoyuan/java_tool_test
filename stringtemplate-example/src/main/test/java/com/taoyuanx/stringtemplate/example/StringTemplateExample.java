package com.taoyuanx.stringtemplate.example;

import com.taoyuanx.stringtemplate.example.entity.User;
import org.junit.Test;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupString;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author dushitaoyuan
 * @desc stringtemplate例子
 * @date 2019/12/13
 */
public class StringTemplateExample {

    @Test
    public void exampleBasic(){

        String template = "<map:{key | <key> }>"; // checks field and method getter
        ST st = new ST(template);
        st.add("map",new LinkedHashMap<String, String>() {{put("key1","v1"); put("key2","v2");}});
        System.out.println(st.render());


        //语法 <attribute.property>
        ST st2 = new ST("Hello, user信息: <user.id>,<user.username>,<user.password>,<user.email>,<user.age>");
        User user=new User(1L,"dushitaoyuan","p1",26,"email1111");
        st2.add("user",user);
        System.out.println(st2.render());
    }
    @Test
    public void exampleOne(){
        ST hello = new ST("Hello, <name>");
        hello.add("name", "World");
        System.out.println(hello.render());


        ST hello1 = new ST("<[names]>");
        hello1.add("names", "1");
        hello1.add("names", "2");
        System.out.println(hello1.render());

        String g = "a(x) ::= <<foo>>\n"+ "b() ::= <<bar>>\n";
        STGroup group = new STGroupString(g);
        System.out.println(group.getInstanceOf("a").render());
        System.out.println(group.getInstanceOf("b").render());

    }
}
