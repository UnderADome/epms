package com.wisdri.epms.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wisdri.epms.Dao.MeetingMapper;
import com.wisdri.epms.Entity.Meeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeetingService {
    @Autowired
    private MeetingMapper meetingMapper;

    /**
     * 存储会议信息
     * @param meeting
     */
    public void SaveMeeting(Meeting meeting){
        meetingMapper.SaveMeeting(meeting);
    }

    /**
     * 分页查询会议相关信息
     * @param page
     * @param pagesize
     * @return
     */
    public Page<Meeting> GetMeetingByPage(int page, int pagesize){
        PageHelper.startPage(page, pagesize);
        return meetingMapper.GetMeetingByPage();
    }

    /**
     * 删除会议信息
     * id string->int
     * @param id
     */
    public void DeleteMeeting(String id){
        meetingMapper.DeleteMeetingById(Integer.parseInt(id));
    }

    /**
     * 查询会议信息 by id
     * @param id
     * @return
     */
    public Meeting GetMeetingById(int id){
        return meetingMapper.GetMeetingById(id);
    }

    /**
     * 更新会议内容 by id
     * @param meeting
     */
    public void UpdateMeeting(Meeting meeting){
        meetingMapper.UpdateMeeting(meeting);
    }
}
