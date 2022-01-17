package com.wisdri.epms.Dao;

import com.wisdri.epms.Entity.Meeting;
import org.apache.ibatis.annotations.*;
import com.github.pagehelper.Page;

@Mapper
public interface MeetingMapper {

    @Insert("Insert into epms.Meeting(id, content, question, partner, meetingTime, infoCreateTime, " +
            "organize, type)" +
            " values (null, #{content}, #{question}, #{partner}, #{meetingTime}, now(), " +
            "#{organize}, #{type})")
    void SaveMeeting(Meeting meeting);

    @Select("select * from meeting")
    @ResultType(Meeting.class)
    Page<Meeting> GetMeetingByPage();

    @Delete("delete from meeting where id = #{id}")
    void DeleteMeetingById(int id);
}
