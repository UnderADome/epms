package com.wisdri.epms.Dao;

import com.github.pagehelper.Page;
import com.wisdri.epms.Entity.Execute;
import com.wisdri.epms.Entity.Plan;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExecuteMapper {

    @Insert("insert into execute(id, planId, leader, content, question, exeStartTime, exeEndTime) " +
            "values (#{id}, #{planId}, #{leader}, #{content}, #{question}, #{exeStartTime}, #{exeEndTime})")
    void SaveExecute(Execute execute);

    @Select("select * from execute")
    @ResultType(Execute.class)
    Page<Execute> GetExecuteByPage();

    @Delete("delete from execute where id = #{id}")
    void DeleteExecuteById(int id);

    @Select("select * from execute where id = #{id}")
    Execute GetExecuteById(int id);

    @Update("update execute set leader = #{leader}, content = #{content}, question = #{question}, " +
            "exeStartTime = #{exeStartTime}, exeEndTime = #{exeEndTime} " +
            "where id = #{id}")
    void UpdateExecute(Execute execute);

    @Update("update execute set finished = 1 where id = #{id}")
    void FinishExecute(int id);

    @Select("select * from execute where planid = #{planid}")
    List<Execute> GetPlansByPlanId(int planid);
}
