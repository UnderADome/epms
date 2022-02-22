package com.wisdri.epms.Dao;

import com.github.pagehelper.Page;
import com.wisdri.epms.Entity.Plan;
import com.wisdri.epms.Entity.Project;
import com.wisdri.epms.Entity.Receive.SearchPlan;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PlanMapper {

    @Insert("insert into plan(id, projectId, leader, content, name, planStartTime, planEndTime, infoCreateTime) " +
            "values (#{id}, #{projectId}, #{leader}, #{content}, #{name}, #{planStartTime}, #{planEndTime}, now())")
    void SavePlan(Plan plan);

    @Select("select * from plan order by plan.infoCreateTime desc")
    @ResultType(Plan.class)
    Page<Plan> GetPlanByPage();

    @Select("<script>" +
            "select plan.* from plan, project where 1=1 " +
            "<if test='name!=null and name!=\"\"'> and plan.name like concat('%',#{name},'%') </if> " +
            "<if test='leader!=null and leader!=\"\"'> and plan.leader like concat('%',#{leader},'%') </if> " +
            "<if test='planStartTime!=null'> and planStartTime like concat('%',#{planStartTime},'%') </if> " +
            "<if test='planEndTime!=null'> and planEndTime like concat('%',#{planEndTime},'%') </if>" +
            "<if test='projectName !=null and projectName !=\"\"'> and project.name like concat('%',#{projectName},'%') </if> " +
            "and plan.projectId = project.id " +
            " order by plan.infoCreateTime desc" +
            "</script>")
    @ResultType(Plan.class)
    Page<Plan> GetPlanByPageAndNormalCondition(SearchPlan plan);

    @Select("<script>" +
            "select plan.* from plan, project where 1=1" +
            "<if test='name!=null and name!=\"\"'> and plan.name like concat('%',#{name},'%') </if> " +
            "<if test='leader!=null and leader!=\"\"'> and plan.leader like concat('%',#{leader},'%') </if> " +
            "<if test='planStartTime!=null and planEndTime!=null'> and planStartTime >= #{planStartTime} and planEndTime &lt;= #{planEndTime} </if> " +
            "<if test='projectName !=null and projectName !=\"\"'> and project.name like concat('%',#{projectName},'%') </if> " +
            "and plan.projectId = project.id " +
            " order by plan.infoCreateTime desc" +
            "</script>")
    @ResultType(Plan.class)
    Page<Plan> GetPlanByPageAndTimeRange(SearchPlan plan);

    //@Delete("delete from plan, execute where plan.id = #{id} and execute.planid = plan.id")
    @Delete("delete plan, execute from plan left join execute on execute.planid = plan.id where plan.id = #{id}")
    void DeletePlanById(int id);

    @Select("select * from plan where id = #{id} order by plan.infoCreateTime desc")
    Plan GetPlanById(int id);

    @Update("update plan set leader = #{leader}, content = #{content}, name = #{name}, " +
            "planStartTime = #{planStartTime}, planEndTime = #{planEndTime} " +
            "where id = #{id}")
    void UpdatePlan(Plan plan);

    @Update("update plan, execute set plan.finished = 1, execute.finished = 1 where plan.id = #{id} and plan.id = execute.planid")
    void FinishPlan(int id);

    @Select("select * from plan where projectid = #{projectid} order by plan.infoCreateTime desc")
    List<Plan> GetPlansByProjectId(int projectid);

    @Update("<script> " +
            "<foreach collection='plans' item='item' index='index' separator=';'>" +
            "update plan set overdue = #{item.overdue} , finished = #{item.finished} where id = #{item.id}" +
            "</foreach>" +
            "</script>")
    void SetOverdueAndFinished(Page<Plan> plans);
}
