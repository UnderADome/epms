package com.wisdri.epms.Dao;

import com.github.pagehelper.Page;
import com.wisdri.epms.Entity.Execute;
import com.wisdri.epms.Entity.Plan;
import com.wisdri.epms.Entity.Receive.SearchExecute;
import com.wisdri.epms.ResultEntity.MonthInfo;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ExecuteMapper {

    @Insert("insert into execute(id, planId, leader, content, question, exeStartTime, exeEndTime, infoCreateTime) " +
            "values (#{id}, #{planId}, #{leader}, #{content}, #{question}, #{exeStartTime}, #{exeEndTime}, now())")
    void SaveExecute(Execute execute);

    @Select("select * from execute order by execute.infoCreateTime desc")
    @ResultType(Execute.class)
    Page<Execute> GetExecuteByPage();

    @Select("<script>" +
            "select execute.* from execute, plan where 1=1 " +
            "<if test='leader!=null and leader!=\"\"'> and execute.leader like concat('%',#{leader},'%') </if> " +
            "<if test='exeStartTime!=null'> and exeStartTime like concat('%',#{exeStartTime},'%') </if> " +
            "<if test='exeEndTime!=null'> and exeEndTime like concat('%',#{exeEndTime},'%') </if>" +
            "<if test='planName !=null and planName !=\"\"'> and plan.name like concat('%',#{planName},'%') </if> " +
            "and execute.planId = plan.id " +
            " order by execute.infoCreateTime desc" +
            "</script>")
    @ResultType(Execute.class)
    Page<Execute> GetExecuteByPageAndNormalCondition(SearchExecute execute);

    @Select("<script>" +
            "select execute.* from execute, plan where 1=1 " +
            "<if test='leader!=null and leader!=\"\"'> and execute.leader like concat('%',#{leader},'%') </if> " +
            "<if test='exeStartTime!=null and exeEndTime!=null'> and exeStartTime >= #{exeStartTime} and exeEndTime &lt;= #{exeEndTime} </if> " +
            "<if test='planName !=null and planName !=\"\"'> and plan.name like concat('%',#{planName},'%') </if> " +
            "and execute.planId = plan.id " +
            " order by execute.infoCreateTime desc" +
            "</script>")
    @ResultType(Execute.class)
    Page<Execute> GetExecuteByPageAndTimeRange(SearchExecute execute);

    @Delete("delete from execute where id = #{id}")
    void DeleteExecuteById(int id);

    @Select("select * from execute where id = #{id} order by execute.infoCreateTime desc")
    Execute GetExecuteById(int id);

    @Update("update execute set leader = #{leader}, content = #{content}, question = #{question}, " +
            "exeStartTime = #{exeStartTime}, exeEndTime = #{exeEndTime} " +
            "where id = #{id}")
    void UpdateExecute(Execute execute);

    @Update("update execute set leader = #{leader}, content = #{content}, question = #{question}, " +
            "exeStartTime = #{exeStartTime}, exeEndTime = #{exeEndTime}, " +
            "exeRealEndTime = #{exeRealEndTime}, finished = 1 " +
            "where id = #{id}")
    void UpdateExecuteWithDone(Execute execute);

    @Update("update execute set leader = #{leader}, content = #{content}, question = #{question}, " +
            "exeStartTime = #{exeStartTime}, exeEndTime = #{exeEndTime}, " +
            "exeRealEndTime = #{exeRealEndTime}, finished = 0 " +
            "where id = #{id}")
    void UpdateExecuteWithNotDone(Execute execute);

    @Update("update execute set finished = 1 where id = #{id}")
    void FinishExecute(int id);

    @Update("update execute set finished = 0, exeRealEndTime = null where id = #{id}")
    void NotdoneExecute(int id);

    @Select("select * from execute where planid = #{planid} order by execute.infoCreateTime desc")
    List<Execute> GetExecutesByPlanId(int planid);

    @Update("<script> " +
            "<foreach collection='executes' item='item' index='index' separator=';'>" +
            "update execute set overdue = #{item.overdue} where id = #{item.id}" +
            "</foreach>" +
            "</script>")
    void SetOverdue(Page<Execute> executes);

    //这里exeRealEndTime修改为了exeStartTime
    @Select("select count(id) from execute where year(exeStartTime) = #{year}")
    int GetExecuteCountByYear(String year);

    @Select("select date_format(exeRealEndTime, '%m') as time, count(id) as executeCount from execute " +
            "where year(exeRealEndTime) = #{year} group by month(exeRealEndTime)")
    List<MonthInfo> GetExecuteCountByYearMonth(String year);

    @Select("select time from (" +
            "select max(exeCount) as count, exeRealEndTime as time from " +
            "(select count(id) as exeCount, date_format(exeRealEndTime, '%Y-%m') as exeRealEndTime from execute " +
            " where year(exeRealEndTime) = #{year} group by month(exeRealEndTime)) as a " +
            ") as b")
    String GetExeMostMonthByYear(String year);
}
