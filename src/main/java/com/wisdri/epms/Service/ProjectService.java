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

    public Page<Project> GetProjectByPageAndCondition(int page, int pagesize, Project project){
        PageHelper.startPage(page, pagesize);
        //分三种情况：
        //1、只对项目名称等字符串进行匹配
        if (project.getProStartTime() == null && project.getProEndTime() == null)
            return projectMapper.GetProjectByPageAndNormalCondition(project);
        //2、有一个时间，直接匹配
        if ((project.getProStartTime() == null && project.getProEndTime() != null) ||
                (project.getProStartTime() != null && project.getProEndTime() == null))
            //调用的方法同上面的
            return projectMapper.GetProjectByPageAndNormalCondition(project);
        //3、有两个时间，范围查询
        else //都不为null，就是时间范围查询
            return projectMapper.GetProjectByPageAndTimeRange(project);
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

    /**
     * 更新已完成状态
     * @param id
     * @return
     */
    public String FinishProject(String id){
        try{
            projectMapper.FinishProject(Integer.parseInt(id));
            return "1";
        }catch (Exception e){
            return "0";
        }
    }
}
