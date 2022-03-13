package com.wisdri.epms.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Data
@Setter
@Getter
/**
 * 定位于一些临时性的工作
 */
public class Platform {
    /**
     * id
     */
    private String id;
    /**
     * 任务名称
     */
    private String name;
    /**
     * 工作内容
     */
    private String content;
    /**
     * 负责人
     */
    private String leader;
    /**
     * 参与人员
     */
    private String partner;
    /**
     * 预计开始时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate platStartTime;
    /**
     * 预计结束时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate platEndTime;
    /**
     * 会议信息创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate platRealEndTime;
    /**
     * 表示是否完成 0未完成 1完成
     */
    private int finished;
    /**
     * 是否超时了 0表示未超时 1表示超时
     */
    private int overdue;
}
