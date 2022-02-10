package com.wisdri.epms.Dao;

import com.github.pagehelper.Page;
import com.wisdri.epms.Entity.Execute;
import com.wisdri.epms.Entity.Plan;
import com.wisdri.epms.Entity.Receive.SearchExecute;
import org.apache.ibatis.annotations.*;

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

    @Update("update execute set finished = 1 where id = #{id}")
    void FinishExecute(int id);

    @Select("select * from execute where planid = #{planid} order by execute.infoCreateTime desc")
    List<Execute> GetPlansByPlanId(int planid);
}
