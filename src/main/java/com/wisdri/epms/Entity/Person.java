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
     * 用户id
     */
    private String id;
    /**
     * 姓名
     */
    private String name;
}
