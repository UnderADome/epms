package com.wisdri.epms.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Setter
@Getter
/**
 * 项目
 */
public class Project {
    /**
     * 项目编号
     */
    private String id;
    /**
     * 项目名称
     */
    private String name;
    /**
     * 项目类型：BIM、研发、开发、平台基础性工作
     */
    private String type;
    /**
     * 项目负责人
     */
    private String leader;
    /**
     * 项目基本信息
     */
    private String content;
    /**
     * 计划开始时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate proStartTime;
    /**
     * 计划结束时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate proEndTime;
    /**
     * 参与人
     */
    private String partner;
    /**
     * 表示是否完成 0未完成 1完成
     */
    private int finished;
}
