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

    @Delete("delete from project where id = #{id}")
    void DeleteProjectById(int id);

    @Select("select * from project where id = #{id}")
    Project GetProjectById(int id);

    @Update("update project set name = #{name}, type = #{type}, leader = #{leader}, content = #{content}, partner = #{partner}, " +
            "proStartTime = #{proStartTime}, proEndTime = #{proEndTime} " +
            "where id = #{id}")
    void UpdateProject(Project project);
}
