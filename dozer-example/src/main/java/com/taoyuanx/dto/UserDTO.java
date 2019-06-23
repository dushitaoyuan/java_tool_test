package com.taoyuanx.dto;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 桃源
 * @description user dto对象
 * @date 2019/6/22
 */
@Data
@ToString
public class UserDTO {
    private String name;
    private String age;
    private String date;
    private LocalDateTime local;
    private String dateTime;
}
