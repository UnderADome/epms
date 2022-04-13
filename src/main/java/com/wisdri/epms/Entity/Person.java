package com.wisdri.epms.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 会议
 */
@Data
@Setter
@Getter
public class Person {
    /**
     * 用户id cn
     */
    private String id;
    /**
     * 姓名 displayName
     */
    private String name;
    /**
     * 密码
     */
    private String password;
    /**
     * 邮箱
     */
    private String mail;
}
