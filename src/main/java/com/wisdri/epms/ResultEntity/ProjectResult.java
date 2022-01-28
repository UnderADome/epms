package com.wisdri.epms.ResultEntity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Setter
@Getter
@ToString
public class ProjectResult {
    /*
                    <th>工程项-名称</th>
                <th>开始时间</th>
                <th>结束时间</th>
                <th>结束时间</th>
                <th>内容</th>
                <th>是否已完成</th>
                <th>是计划还是实施</th>
     */

    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate endTime;
    private String content;
    private int done;
    private int itemType;

}
