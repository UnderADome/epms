package com.wisdri.epms.Dao;

import com.github.pagehelper.Page;
import com.wisdri.epms.Entity.Execute;
import com.wisdri.epms.Entity.Project;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ProjectMapper {

    @Insert("insert into project(id, name, type, leader, content, partner, proStartTime, proEndTime, infoCreateTime) " +
            "values (#{id}, #{name}, #{type}, #{leader}, #{content}, #{partner}, #{proStartTime}, #{proEndTime}, now())")
    void SaveProject(Project project);

    @Select("select * from project order by project.infoCreateTime desc")
    @ResultType(Project.class)
    Page<Project> GetProjectByPage();

    @Select("<script>" +
            "select * from project where 1=1" +
            "<if test='name!=null and name!=\"\"'> and name like concat('%',#{name},'%') </if> " +
            "<if test='type!=null and type!=\"\"'> and type like concat('%',#{type},'%') </if> " +
            "<if test='leader!=null and leader!=\"\"'> and leader like concat('%',#{leader},'%') </if> " +
            "<if test='proStartTime!=null'> and proStartTime like concat('%',#{proStartTime},'%') </if> " +
            "<if test='proEndTime!=null'> and proEndTime like concat('%',#{proEndTime},'%') </if> " +
            "order by project.infoCreateTime desc" +
            "</script>")  //时间类型不做空串判断
    @ResultType(Project.class)
    Page<Project> GetProjectByPageAndNormalCondition(Project project);

    @Select("<script>" +
            "select * from project where 1=1 " +
            "<if test='name!=null and name!=\"\"'> and name like concat('%',#{name},'%') </if> " +
            "<if test='type!=null and type!=\"\"'> and type like concat('%',#{type},'%') </if> " +
            "<if test='leader!=null and leader!=\"\"'> and leader like concat('%',#{leader},'%') </if> " +
            "<if test='proStartTime!=null and proEndTime!=null'> and proStartTime >= #{proStartTime} and proEndTime &lt;= #{proEndTime} </if> " +
            " order by project.infoCreateTime desc" +
            //"<if test='proStartTime!=null and proEndTime!=null'> and proEndTime &lt;= #{proEndTime} </if> " +
        "</script>")                                                     //这里的<=改写成&lt;=
    @ResultType(Project.class)                                           //原因是mybatis采用的什么ongl，会把<当成标签符号，所以需要转义
    Page<Project> GetProjectByPageAndTimeRange(Project project);

    @Delete("delete plan, execute, project from " +
            "plan left join execute on execute.planid = plan.id  " +
            "    right join project on project.id = plan.projectid " +
            "where projectid = #{id}")
    void DeleteProjectById(int id);

    @Select("select * from project where id = #{id} order by project.infoCreateTime desc")
    Project GetProjectById(int id);

    @Update("update project set name = #{name}, type = #{type}, leader = #{leader}, content = #{content}, partner = #{partner}, " +
            "proStartTime = #{proStartTime}, proEndTime = #{proEndTime} " +
            "where id = #{id}")
    void UpdateProject(Project project);

    @Update("update project, plan, execute set project.finished = 1, plan.finished = 1, execute.finished = 1 " +
            "where project.id = #{id} and project.id = plan.projectid and plan.id = execute.planid")
    void FinishProject(int id);

    @Select("select id from project where name = #{projectName} order by project.infoCreateTime desc")
    int FindProjectIdByName(String projectName);

    @Select("select * from project where name = #{projectName} order by project.infoCreateTime desc")
    Project FindProjectByProjectName(String projectName);

    @Update("<script> " +
            "<foreach collection='projects' item='item' index='index' separator=';'>" +
            "update project set overdue = #{item.overdue}, finished = #{item.finished} where id = #{item.id}" +
            "</foreach>" +
            "</script>")
    void SetOverdueAndFinished(Page<Project> projects);
}
