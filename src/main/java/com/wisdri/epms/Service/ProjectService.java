package com.wisdri.epms.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wisdri.epms.Dao.ProjectMapper;
import com.wisdri.epms.Entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    @Autowired
    private ProjectMapper projectMapper;

    /**
     * 存储项目信息
     * @param project
     */
    public void SaveProject(Project project){
        projectMapper.SaveProject(project);
    }

    /**
     * 分页查询项目相关信息
     * @param page
     * @param pagesize
     * @return
     */
    public Page<Project> GetProjectByPage(int page, int pagesize){
        PageHelper.startPage(page, pagesize);
        return projectMapper.GetProjectByPage();
    }

    /**
     * 删除项目信息
     * id string->int
     * @param id
     */
    public void DeleteProject(String id){
        projectMapper.DeleteProjectById(Integer.parseInt(id));
    }

    /**
     * 查询项目信息 by id
     * @param id
     * @return
     */
    public Project GetProjectById(int id){
        return projectMapper.GetProjectById(id);
    }

    /**
     * 更新项目内容 by id
     * @param project
     */
    public void UpdateProject(Project project){
        projectMapper.UpdateProject(project);
    }
}
