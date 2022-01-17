package com.wisdri.epms.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 会议
 */
@Data
@Setter
@Getter
public class Meeting {
    /**
     * id
     */
    public String id;
    /**
     * 会议纪要
     */
    public String content;
    /**
     * 会议中提出的问题
     */
    public String question;
    /**
     * 参与人员
     */
    public String partner;
    /**
     * 开会时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "GMT+8")
    public LocalDate meetingTime;
    /**
     * 会议信息创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "GMT+8")
    public LocalDate infoCreateTime;
    /**
     * 组织者
     */
    public String organize;
    /**
     * 上传的图片
     */
    public List<String> picList;
    /**
     * 会议类型
     */
    public String type;
}
