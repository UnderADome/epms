package com.wisdri.epms.Dao;

import com.github.pagehelper.Page;
import com.wisdri.epms.Entity.Project;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ProjectMapper {

    @Insert("insert into project(id, name, type, leader, content, partner, proStartTime, proEndTime) " +
            "values (#{id}, #{name}, #{type}, #{leader}, #{content}, #{partner}, #{proStartTime}, #{proEndTime})")
    void SaveProject(Project project);

    @Select("select * from project")
    @ResultType(Project.class)
    Page<Project> GetProjectByPage();

    @Delete("delete plan, execute, project from " +
            "plan left join execute on execute.planid = plan.id  " +
            "    right join project on project.id = plan.projectid " +
            "where projectid = #{id}")
    void DeleteProjectById(int id);

    @Select("select * from project where id = #{id}")
    Project GetProjectById(int id);

    @Update("update project set name = #{name}, type = #{type}, leader = #{leader}, content = #{content}, partner = #{partner}, " +
            "proStartTime = #{proStartTime}, proEndTime = #{proEndTime} " +
            "where id = #{id}")
    void UpdateProject(Project project);

    @Update("update project, plan, execute set project.finished = 1, plan.finished = 1, execute.finished = 1 " +
            "where project.id = #{id} and project.id = plan.projectid and plan.id = execute.planid")
    void FinishProject(int id);

    @Select("select id from project where name = #{projectName}")
    int FindProjectIdByName(String projectName);

    @Select("select * from project where name = #{projectName}")
    Project FindProjectByProjectName(String projectName);
}
