package com.wisdri.epms.Dao;

import com.github.pagehelper.Page;
import com.wisdri.epms.Entity.Plan;
import org.apache.ibatis.annotations.*;

@Mapper
public interface PlanMapper {

    @Insert("insert into plan(id, projectId, leader, content, name, planStartTime, planEndTime) " +
            "values (#{id}, #{projectId}, #{leader}, #{content}, #{name}, #{planStartTime}, #{planEndTime})")
    void SavePlan(Plan plan);

    @Select("select * from plan")
    @ResultType(Plan.class)
    Page<Plan> GetPlanByPage();

    @Delete("delete from plan where id = #{id}")
    void DeletePlanById(int id);

    @Select("select * from plan where id = #{id}")
    Plan GetPlanById(int id);

    @Update("update plan set leader = #{leader}, content = #{content}, name = #{name}, " +
            "planStartTime = #{planStartTime}, planEndTime = #{planEndTime} " +
            "where id = #{id}")
    void UpdatePlan(Plan plan);
}
