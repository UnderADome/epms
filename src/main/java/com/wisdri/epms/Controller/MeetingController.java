package com.wisdri.epms.Controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.wisdri.epms.Entity.Meeting;
import com.wisdri.epms.Service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MeetingController {

    @Autowired
    public MeetingService meetingService;

    @RequestMapping(value="epmsview/ReadMeetingInfo")
    public String ShowReadMeetingInfo(){
        return "epmsview/meeting/ReadMeetingInfo";
    }
    @RequestMapping(value="epmsview/AddMeeting")
    public String ShowAddMeeting(){
        return "epmsview/meeting/AddMeeting";
    }
    @RequestMapping(value="epmsview/EditMeeting")
    public ModelAndView ShowEditMeetingContent(@RequestParam String id){
        System.out.println("显示Edit画面：" + id);
        ModelAndView view = new ModelAndView();
        //查询
        Meeting meeting = meetingService.GetMeetingById(Integer.parseInt(id));
        System.out.println(meeting.meetingTime);
        view.addObject(meeting);
        view.setViewName("epmsview/meeting/EditMeeting.html");
        return view;
    }

    /**
     * 提交会议的具体内容
     * @param meeting
     */
    @RequestMapping(value="meeting/SubmitMeeting", method = RequestMethod.POST)
    @ResponseBody
    public String SubmitMeeting(@RequestBody Meeting meeting){
        System.out.println(meeting.meetingTime + " " + meeting.content);
        try{
            meetingService.SaveMeeting(meeting);
        }
        catch (Exception e){
            e.printStackTrace();
            return "0";
        }
        return "1";  //返回1表示成功
    }

    /**
     * 获取会议信息
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value="meeting/ReadMeetingInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> ReadMeetingInfo(@RequestParam("page") String page, @RequestParam("limit") String limit,
                                               @RequestParam(value = "meeting", required = false)String meeting){
        System.out.println("查询会议基本信息");
        Meeting meeting1 = JSON.parseObject(meeting, Meeting.class);
        Page<Meeting> mResultList = new Page<Meeting>();
        if (meeting1 == null)
            mResultList = meetingService.GetMeetingByPage(Integer.parseInt(page), Integer.parseInt(limit));
        else
            mResultList = meetingService.GetMeetingByPageAndCondition(Integer.parseInt(page), Integer.parseInt(limit), meeting1);
        //按照layui需要的标准格式进行封装
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "消息传递成功");
        map.put("count", mResultList.size());
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

    /**
     * 更新会议内容
     * @param meeting
     * @return
     */
    @RequestMapping(value="meeting/UpdateMeeting", method = RequestMethod.POST)
    @ResponseBody
    public String UpdateMeeting(@RequestBody Meeting meeting){
        try{
            meetingService.UpdateMeeting(meeting);
        }
        catch (Exception e){
            e.printStackTrace();
            return "0";
        }
        return "1";  //返回1表示成功
    }

}
