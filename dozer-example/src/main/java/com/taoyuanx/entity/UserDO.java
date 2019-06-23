package com.taoyuanx.entity;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 桃源
 * @description 用户DO对象
 * @date 2019/6/22
 */
@Data
@ToString
public class UserDO {
   private String name;
   private String age;
   private Date date;
   private Date local;
   private LocalDateTime dateTime;
}
