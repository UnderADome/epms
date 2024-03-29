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
 * 实施
 */
public class Execute {
        /**
         * 实施id
         */
        private String id;
        /**
         * 关联的计划id
         */
        private String planId;
        /**
         * 实施人
         */
        private String leader;
        /**
         * 实施的具体内容
         */
        private String content;
        /**
         * 实施过程中遇到的问题
         */
        private String question;
        /**
         * 实施开始时间
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "GMT+8")
        private LocalDate exeStartTime;
        /**
         * 实施结束时间
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "GMT+8")
        private LocalDate exeEndTime;
        /**
         * 实施真正结束的时间
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "GMT+8")
        private LocalDate exeRealEndTime;
        /**
         * 表示是否完成 0未完成 1完成
         */
        private int finished;
        /**
         * 表示是否逾期 0未逾期 1逾期
         */
        private int overdue;
}
