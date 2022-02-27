package com.wisdri.epms.Dao;

import com.wisdri.epms.Entity.Meeting;
import com.wisdri.epms.ResultEntity.MonthInfo;
import org.apache.ibatis.annotations.*;
import com.github.pagehelper.Page;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Mapper
public interface MeetingMapper {

    @Insert("Insert into epms.Meeting(id, content, question, partner, meetingTime, infoCreateTime, " +
            "organize, type)" +
            " values (null, #{content}, #{question}, #{partner}, #{meetingTime}, now(), " +
            "#{organize}, #{type})")
    void SaveMeeting(Meeting meeting);

    @Select("select * from meeting order by meetingTime desc")
    @ResultType(Meeting.class)
    Page<Meeting> GetMeetingByPage();

    @Delete("delete from meeting where id = #{id}")
    void DeleteMeetingById(int id);

    @Select("select * from meeting where id = #{id}")
    Meeting GetMeetingById(int id);

    @Update("update meeting set content = #{content}, question = #{question}, partner = #{partner}, " +
            "meetingTime = #{meetingTime}, infoCreateTime = #{infoCreateTime}, organize = #{organize}, type = #{type} " +
            "where id = #{id}")
    void UpdateMeeting(Meeting meeting);

    @Select("select date_format(meetingTime, '%m') as time, count(id) as meetingCount from meeting " +
            "where year(meetingTime) = #{year} group by month(meetingTime)")
    List<MonthInfo> GetMeetingCountByYearMonth(String year);
}
