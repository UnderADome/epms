package com.wisdri.epms.ResultEntity;

import com.alibaba.fastjson.JSONArray;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class AnnualProject {
    /**
     * 年度项目数量
     */
    private int projectNum;
    /**
     * 年度执行数量
     */
    private int executeNum;
    /**
     * 年度会议数量
     */
    private int meetingNum;
    /**
     * 年度培训数量
     */
    private int trainNum;
    /**
     * 年度执行数量最多的一个月
     */
    private String exeMostMonth;
    /**
     * 年度项目周期最长的一个项目
     */
    private String longestProject;

    /**
     * 月度信息
     */
    private List<MonthInfo> monthInfoList;

    /**
     * 一个人某年每个月的实施量的情况统计
     */
    private JSONArray personExecuteMonthInfo;

    /**
     * 用户名称（实施者）信息
     */
    private String[] nameArray;
}
