package com.wisdri.epms.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * 培训
 */
@Data
@Setter
@Getter
public class Train {
    /**
     * id
     */
    public String id;
    /**
     * 培训纪要
     */
    public String content;
    /**
     * 培训中提出的问题
     */
    public String question;
    /**
     * 参与人员
     */
    public String partner;
    /**
     * 培训时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "GMT+8")
    public LocalDate trainTime;
    /**
     * 培训信息创建时间
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
     * 培训类型
     */
    public String type;
}
