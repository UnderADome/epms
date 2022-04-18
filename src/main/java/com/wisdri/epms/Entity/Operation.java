package com.wisdri.epms.Entity;

import lombok.Data;

/**
 * @author 李韬 @date 2022/4/6 10:38
 * @description 记录用户操作
 */
@Data
public class Operation {
    /**
     * 操作id
     */
    private int id;
    /**
     * 操作用户id
     */
    private String personid;
    /**
     * 客户端IP
     */
    private String ip;
    /**
     * 具体的操作
     * 在DB中text类型
     */
    private String action;
    /**
     * 操作时间
     */
    private String operateTime;
    /**
     * 标识该操作是否成功 1 成功 0 未成功
     */
    private int successFlag;
}
