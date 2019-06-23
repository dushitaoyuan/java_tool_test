package com.taoyuanx;

import com.taoyuanx.dozer.CBeanMapper;
import com.taoyuanx.dto.UserDTO;
import com.taoyuanx.entity.UserDO;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 桃源
 * @description  dozer测试
 * @date 2019/6/22
 */
public class DozerTest {

    @Test
    public void testDateString(){
        UserDO userDO=new UserDO();
        userDO.setDate(new Date());
        System.out.println(  CBeanMapper.map(userDO, UserDTO.class));
        System.out.println(  CBeanMapper.map(userDO, UserDTO.class,(s,d)->{
            d.setDate("后处理");
        }));
    }
    @Test
    public void testDateLocalDate(){
        UserDO userDO=new UserDO();
        userDO.setLocal(new Date());
        System.out.println(  CBeanMapper.map(userDO, UserDTO.class));
        System.out.println(  CBeanMapper.map(userDO, UserDTO.class,(s,d)->{
            d.setDate("后处理");
        }));
    }
    @Test
    public void testDateLocalString(){
        UserDO userDO=new UserDO();
        userDO.setDateTime(LocalDateTime.now());
        System.out.println(  CBeanMapper.map(userDO, UserDTO.class));
        System.out.println(  CBeanMapper.map(userDO, UserDTO.class,(s,d)->{
            d.setDate("后处理");
        }));
    }
}
