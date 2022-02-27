package com.wisdri.epms.ResultEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
/**
 * 月度信息
 */
public class MonthInfo {
    /**
     * 月份
     */
    private String time;
    /**
     * 月度实施数量
     */
    private int executeCount;
    /**
     * 月度会议数量
     */
    private int meetingCount;
    /**
     * 月度培训数量
     */
    private int trainCount;
}
