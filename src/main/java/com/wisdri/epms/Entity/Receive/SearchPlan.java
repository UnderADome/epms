package com.wisdri.epms.Entity.Receive;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Setter
@Getter
/**
 * 用于查询所需计划 Plan
 */
public class SearchPlan {
    /**
     * 计划id
     */
    private String id;
    /**
     * 关联的项目名称 name
     */
    private String projectName;
    /**
     * 关联的项目id
     */
    private String projectId;
    /**
     * 计划人
     */
    private String leader;
    /**
     * 计划项（计划简短描述、计划名称）
     */
    private String name;
    /**
     * 计划的具体内容
     */
    private String content;
    /**
     * 计划开始时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate planStartTime;
    /**
     * 计划结束时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate planEndTime;
    /**
     * 表示是否完成 0未完成 1完成
     */
    private int finished;
}
