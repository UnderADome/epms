package com.wisdri.epms.Controller;

import com.github.pagehelper.Page;
import com.wisdri.epms.Entity.Meeting;
import com.wisdri.epms.Entity.TTest;
import com.wisdri.epms.ResultEntity.MeetingResult;
import com.wisdri.epms.ResultEntity.WangEditor;
import com.wisdri.epms.Service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MeetingController {

    @Autowired
    public MeetingService meetingService;

    @RequestMapping("project/{content}")
    public String ShowMeetingPages(@PathVariable("content") String content){
        System.out.println("会议页面："+content);
        if (content.contains("ReadMeetingInfo"))
            return "project/"+content;
        else if (content.contains("AddMeeting"))
            return "project/"+content;
        else
            return "project/404Error";
    }

    /**
     * 批量存储从富文本中上传的图片
     * @param multipartFiles
     * @param request
     * @return
     */
    @RequestMapping(value = "meeting/submitMulti", method = RequestMethod.POST)
    @ResponseBody
    public WangEditor UploadMultiPicture(@RequestParam("pictures") MultipartFile[] multipartFiles, HttpServletRequest request){
        System.out.println("图片上传功能开始");
        ArrayList<String> values = new ArrayList<String>();
        for (int i=0; i< multipartFiles.length; i++){
            System.out.println(multipartFiles[i].getName());
            try {
                //拿到图片
                byte[] bytes = multipartFiles[i].getBytes();
                //设置图片的存储位置
                String path = ResourceUtils.getURL("classpath:static").getPath() + "/pictures/";
                //处理成UTF8的URL
                path = URLDecoder.decode(path,"utf-8");
                File imgFile = new File(path);
                if (!imgFile.exists()) {
                    imgFile.mkdirs();
                }
                String fileName = multipartFiles[i].getOriginalFilename();// 文件名称
                //可以不用传递到数据库中，在富文本中就已经存在了图片的URL
                System.out.println(path + fileName);

                try (FileOutputStream fos = new FileOutputStream(new File(path + fileName))) {
                    int len = 0;
                    fos.write(bytes);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String value = "http://localhost:8080/pictures/" + fileName;
                values.add(value);

            }catch (Exception e){
                //return null;  //似乎没有什么必要
            }
        }
        WangEditor wangEditor = new WangEditor();
        wangEditor.setCode(0);
        wangEditor.setMsg("上传成功");
        wangEditor.setData(values.toArray(new String[values.size()]));

        System.out.println(wangEditor.toString());
        return wangEditor;
    }

    /**
     * 提交会议的具体内容
     * @param meeting
     */
    @RequestMapping(value="meeting/SubmitMeeting", method = RequestMethod.POST)
    @ResponseBody
    public void SubmitMeeting(@RequestBody Meeting meeting){
        System.out.println(meeting.meetingTime + " " + meeting.content);
        meetingService.SaveMeeting(meeting);
    }

    /**
     * 获取会议信息
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value="meeting/ReadMeetingInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> ReadMeetingInfo(@RequestParam("page") String page, @RequestParam("limit") String limit){
        System.out.println("查询会议基本信息");

        Page<Meeting> mResultList = meetingService.GetMeetingByPage(Integer.parseInt(page), Integer.parseInt(limit));
        //按照layui需要的标准格式进行封装
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "消息传递成功");
        map.put("count", "100");
        map.put("data", mResultList);

        return  map;
    }

    /**
     * 删除会议信息
     * 成功返回1 失败返回0
     * @return
     */
    @RequestMapping(value="meeting/DeleteMeeting", method = RequestMethod.POST)
    @ResponseBody
    public void DeleteMeeting(@RequestParam String id){  //并非一定要指定@RequestParam的详细参数
        System.out.println("执行删除函数, 获取的id："+id);
        meetingService.DeleteMeeting(id);
    }

    @RequestMapping(value="meeting/EditMeeting", method = RequestMethod.POST)
    public String ShowEditMeeting(@RequestParam String id){
        System.out.println("显示Edit画面：" + id);
        return "project/EditMeeting.html";
    }

}
