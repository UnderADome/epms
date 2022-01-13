package com.wisdri.epms.ResultEntity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 会议
 */
@Data
@Setter
@Getter
public class MeetingResult {
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
     * 会议开启时间
     */
    public LocalDateTime meetingTime;
    /**
     * 会议信息创建时间
     */
    public LocalDateTime infoCreateTime;
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
