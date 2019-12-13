package com.taoyuanx.stringtemplate.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author dushitaoyuan
 * @date 2019/12/13
 */
@Data
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password;
    private Integer age;
    private String email;
}
