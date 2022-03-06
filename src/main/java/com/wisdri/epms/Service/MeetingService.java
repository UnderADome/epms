package com.wisdri.epms.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wisdri.epms.Dao.MeetingMapper;
import com.wisdri.epms.Entity.Meeting;
import com.wisdri.epms.ResultEntity.MonthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * 带条件查询会议相关信息
     * @param page
     * @param pagesize
     * @param meeting
     * @return
     */
    public Page<Meeting> GetMeetingByPageAndCondition(int page, int pagesize, Meeting meeting){
        PageHelper.startPage(page, pagesize);
        return meetingMapper.GetMeetingByPageAndCondition(meeting);
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

    /**
     * 获得年度-月 会议的数量
     * @param year
     * @return
     */
    public List<MonthInfo> GetMeetingCountByYearMonth(String year){
        return meetingMapper.GetMeetingCountByYearMonth(year);
    }

    /**
     * 得到总数
     * @return
     */
    public int GetAllCountOfMeeting(){
        return meetingMapper.GetAllCountOfMeeting();
    }
}
