package com.wisdri.epms.Dao;

import com.github.pagehelper.Page;
import com.wisdri.epms.Entity.Plan;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PlanMapper {

    @Insert("insert into plan(id, projectId, leader, content, name, planStartTime, planEndTime) " +
            "values (#{id}, #{projectId}, #{leader}, #{content}, #{name}, #{planStartTime}, #{planEndTime})")
    void SavePlan(Plan plan);

    @Select("select * from plan")
    @ResultType(Plan.class)
    Page<Plan> GetPlanByPage();

    //@Delete("delete from plan, execute where plan.id = #{id} and execute.planid = plan.id")
    @Delete("delete plan, execute from plan left join execute on execute.planid = plan.id where plan.id = #{id}")
    void DeletePlanById(int id);

    @Select("select * from plan where id = #{id}")
    Plan GetPlanById(int id);

    @Update("update plan set leader = #{leader}, content = #{content}, name = #{name}, " +
            "planStartTime = #{planStartTime}, planEndTime = #{planEndTime} " +
            "where id = #{id}")
    void UpdatePlan(Plan plan);

    @Update("update plan, execute set plan.finished = 1, execute.finished = 1 where plan.id = #{id} and plan.id = execute.planid")
    void FinishPlan(int id);

    @Select("select * from plan where projectid = #{projectid}")
    List<Plan> GetPlansByProjectId(int projectid);
}
