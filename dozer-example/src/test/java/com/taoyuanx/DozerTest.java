package com.taoyuanx;

import com.taoyuanx.beanmapper.CBeanMapper;
import com.taoyuanx.dto.UserDTO;
import com.taoyuanx.entity.UserDO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author 桃源
 * @description dozer测试
 * @date 2019/6/22
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread) //Thread: 该状态为每个线程独享。
public class DozerTest {

    @Test
    public void testDateString() {
        UserDO userDO = new UserDO();
        userDO.setDate(new Date());
        System.out.println(CBeanMapper.map(userDO, UserDTO.class));
        System.out.println(CBeanMapper.map(userDO, UserDTO.class, (s, d) -> {
            d.setDate("后处理");
        }));
    }

    @Test
    public void testDateLocalDate() {
        UserDO userDO = new UserDO();
        userDO.setLocal(new Date());
        System.out.println(CBeanMapper.map(userDO, UserDTO.class));
        System.out.println(CBeanMapper.map(userDO, UserDTO.class, (s, d) -> {
            d.setDate("后处理");
        }));
    }


    @Test
    public void testDateLocalString() {
        UserDO userDO = new UserDO();
        userDO.setDateTime(LocalDateTime.now());
        UserDTO map = CBeanMapper.map(userDO, UserDTO.class);
        UserDTO map1 = CBeanMapper.map(userDO, UserDTO.class, (s, d) -> {
            d.setDate("后处理");
        });
        System.out.println(map);
        System.out.println(map1);

    }

    @Benchmark

    public void testBeanUtil() throws Exception {
        UserDO userDO = new UserDO();
        userDO.setAge("12");
        userDO.setName("name");
        BeanUtils.copyProperties(userDO, new UserDTO());


    }

    @Benchmark
    public void testPropertyUtils() throws Exception {
        UserDO userDO = new UserDO();
        userDO.setAge("12");
        userDO.setName("name");
        PropertyUtils.copyProperties(userDO, new UserDTO());


    }

    @Benchmark
    public void testDozer() {
        UserDO userDO = new UserDO();
        userDO.setAge("12");
        userDO.setName("name");
        UserDTO map1 = CBeanMapper.map(userDO, UserDTO.class);

    }

    @Benchmark
    public void testSpringBeanUtil() {
        UserDO userDO = new UserDO();
        userDO.setAge("12");
        userDO.setName("name");
        org.springframework.beans.BeanUtils.copyProperties(userDO, new UserDTO());


    }


    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(DozerTest.class.getSimpleName()) //benchmark 所在的类的名字，注意这里是使用正则表达式对所有类进行匹配的
                .forks(1) //进行 fork 的次数。如果 fork 数是2的话，则 JMH 会 fork 出两个进程来进行测试
                .warmupIterations(1) //预热的迭代次数
                .measurementIterations(1) //实际测量的迭代次数
                .measurementBatchSize(100)
                .warmupBatchSize(100)
                .build();
        new Runner(opt).run();

    }
}
