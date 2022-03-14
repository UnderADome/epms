package com.wisdri.epms.Dao;

import com.github.pagehelper.Page;
import com.wisdri.epms.Entity.Platform;
import com.wisdri.epms.ResultEntity.MonthInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PlatformMapper {


    @Insert("insert into platform(id, name, content, leader, partner, platStartTime, platEndTime, platRealEndTime, infoCreateTime) " +
            "values (#{id}, #{name}, #{content}, #{leader}, #{partner}, #{platStartTime}, #{platEndTime}, #{platRealEndTime}, now())")
    void SavePlatform(Platform platform);

    @Select("select * from platform order by platform.infoCreateTime desc")
    @ResultType(Platform.class)
    Page<Platform> GetPlatformByPage();

    @Select("<script>" +
            "select platform.* from platform, plan where 1=1 " +
            "<if test='leader!=null and leader!=\"\"'> and platform.leader like concat('%',#{leader},'%') </if> " +
            "<if test='platStartTime!=null'> and platStartTime like concat('%',#{platStartTime},'%') </if> " +
            "<if test='platEndTime!=null'> and platEndTime like concat('%',#{platEndTime},'%') </if>" +
            "<if test='planName !=null and planName !=\"\"'> and plan.name like concat('%',#{planName},'%') </if> " +
            "and platform.planId = plan.id " +
            " order by platform.infoCreateTime desc" +
            "</script>")
    @ResultType(Platform.class)
    Page<Platform> GetPlatformByPageAndCondition(Platform platform);

    @Select("<script>" +
            "select platform.* from platform, plan where 1=1 " +
            "<if test='leader!=null and leader!=\"\"'> and platform.leader like concat('%',#{leader},'%') </if> " +
            "<if test='platStartTime!=null and platEndTime!=null'> and platStartTime >= #{platStartTime} and platEndTime &lt;= #{platEndTime} </if> " +
            "<if test='planName !=null and planName !=\"\"'> and plan.name like concat('%',#{planName},'%') </if> " +
            "and platform.planId = plan.id " +
            " order by platform.infoCreateTime desc" +
            "</script>")
    @ResultType(Platform.class)
    Page<Platform> GetPlatformByPageAndTimeRange(Platform platform);

    @Delete("delete from platform where id = #{id}")
    void DeletePlatformById(int id);

    @Select("select * from platform where id = #{id} order by platform.infoCreateTime desc")
    Platform GetPlatformById(int id);

    @Update("update platform set name = #{name}, content = #{content}, leader = #{leader}, partner = #{partner}, " +
            "platStartTime = #{platStartTime}, platEndTime = #{platEndTime}, platRealEndTime = #{platRealEndTime} " +
            "where id = #{id}")
    void UpdatePlatform(Platform platform);

    @Update("update platform set name = #{name}, content = #{content}, leader = #{leader}, partner = #{partner}, " +
            "platStartTime = #{platStartTime}, platEndTime = #{platEndTime}, " +
            "platRealEndTime = #{platRealEndTime}, finished = 1 " +
            "where id = #{id}")
    void UpdatePlatformWithDone(Platform platform);

    @Update("update platform set name = #{name}, content = #{content}, leader = #{leader}, partner = #{partner}, " +
            "platStartTime = #{platStartTime}, platEndTime = #{platEndTime}, " +
            "platRealEndTime = #{platRealEndTime}, finished = 0 " +
            "where id = #{id}")
    void UpdatePlatformWithNotDone(Platform platform);

    @Update("update platform set finished = 1 where id = #{id}")
    void FinishPlatform(int id);

    @Update("update platform set finished = 0, platRealEndTime = null where id = #{id}")
    void NotdonePlatform(int id);

    @Select("select * from platform where planid = #{planid} order by platform.infoCreateTime desc")
    List<Platform> GetPlatformsByPlanId(int planid);

    @Update("<script> " +
            "<foreach collection='platforms' item='item' index='index' separator=';'>" +
            "update platform set overdue = #{item.overdue} where id = #{item.id}" +
            "</foreach>" +
            "</script>")
    void SetOverdue(Page<Platform> platforms);

    @Update("update platform set overdue = #{overdue} where id = #{id}")
    void SetOverdueOfOnePlatform(Platform platform);

    //这里platRealEndTime修改为了platStartTime
    @Select("select count(id) from platform where year(platStartTime) = #{year}")
    int GetPlatformCountByYear(String year);

    @Select("select date_format(platRealEndTime, '%m') as time, count(id) as platformCount from platform " +
            "where year(platRealEndTime) = #{year} group by month(platRealEndTime)")
    List<MonthInfo> GetPlatformCountByYearMonth(String year);

    @Select("select time from (" +
            "select max(platCount) as count, platRealEndTime as time from " +
            "(select count(id) as platCount, date_format(platRealEndTime, '%Y-%m') as platRealEndTime from platform " +
            " where year(platRealEndTime) = #{year} group by month(platRealEndTime)) as a " +
            ") as b")
    String GetExeMostMonthByYear(String year);

    @Select("select count(id) from platform")
    int GetAllCountOfPlatform();
}
