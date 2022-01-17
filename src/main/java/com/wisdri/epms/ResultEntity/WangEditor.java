package com.wisdri.epms.ResultEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;

@Getter
@Setter
@ToString
public class WangEditor {

    private Integer code; //错误代码，0 表示没有错误。
    private String msg;  //传递消息
    private String[] data; //已上传的图片路径

}