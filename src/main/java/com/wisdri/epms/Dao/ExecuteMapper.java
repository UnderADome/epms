package com.wisdri.epms.Dao;

import com.github.pagehelper.Page;
import com.wisdri.epms.Entity.Execute;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ExecuteMapper {

    @Insert("insert into execute(id, plantId, leader, content, question, exeStartTime, exeEndTime) " +
            "values (#{id}, #{plantId}, #{leader}, #{content}, #{question}, #{exeStartTime}, #{exeEndTime})")
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
}
