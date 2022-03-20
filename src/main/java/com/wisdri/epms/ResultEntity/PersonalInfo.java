package com.wisdri.epms.ResultEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
/**
 * 每个人的情况
 * 目前只统计当负责人的情况
 */
public class PersonalInfo {
    /**
     * 事件的负责人
     */
    private String leader;
    /**
     * 月份
     */
    private String time;
    /**
     * 月度实施数量
     */
    private int executeCount;
}
