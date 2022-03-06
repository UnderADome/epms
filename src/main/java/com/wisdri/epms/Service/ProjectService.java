package com.wisdri.epms.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wisdri.epms.Dao.ExecuteMapper;
import com.wisdri.epms.Dao.PlanMapper;
import com.wisdri.epms.Dao.ProjectMapper;
import com.wisdri.epms.Entity.Execute;
import com.wisdri.epms.Entity.Plan;
import com.wisdri.epms.Entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private PlanMapper planMapper;
    @Autowired
    private ExecuteMapper executeMapper;

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
        Page<Project> projects = projectMapper.GetProjectByPage();
        projects = CalIfOverdue(projects);
        return projects;
    }

    public Page<Project> GetProjectByPageAndCondition(int page, int pagesize, Project project){
        PageHelper.startPage(page, pagesize);
        //分三种情况：
        //1、只对项目名称等字符串进行匹配
        if (project.getProStartTime() == null && project.getProEndTime() == null){
            Page<Project> projects = projectMapper.GetProjectByPageAndNormalCondition(project);
            projects = CalIfOverdue(projects);
            return projects;
        }
        //2、有一个时间，直接匹配
        if ((project.getProStartTime() == null && project.getProEndTime() != null) ||
                (project.getProStartTime() != null && project.getProEndTime() == null)){
            //调用的方法同上面的
            Page<Project> projects = projectMapper.GetProjectByPageAndNormalCondition(project);
            projects = CalIfOverdue(projects);
            return projects;
        }
        //3、有两个时间，范围查询
        else { //都不为null，就是时间范围查询
            Page<Project> projects = projectMapper.GetProjectByPageAndTimeRange(project);
            projects = CalIfOverdue(projects);
            return projects;
        }
    }

    public Page<Project> CalIfOverdue(Page<Project> projects){
        for (Project project : projects){
            //根据project查询plans
            List<Plan> planList = planMapper.GetPlansByProjectId(Integer.parseInt(project.getId()));
            //表示有多少个plan完成了
            int finishedCount = 0;
            for (int i=0; i<planList.size(); i++){
                //去查询阶段中的实施，如果有一个实施的实际完成时间，比项目预定的完成时间要晚，即表示项目延期了
                List<Execute> executeList = executeMapper.GetExecutesByPlanId(Integer.parseInt(planList.get(i).getId()));
                for (Execute execute : executeList){
                    if (execute.getExeRealEndTime() != null && project.getProEndTime() != null)
                        if (execute.getExeRealEndTime().isAfter(project.getProEndTime()))
                            project.setOverdue(1);
                }

                if (planList.get(i).getFinished() == 0)
                    project.setFinished(0);
                else
                    finishedCount++;
            }
            //所有的阶段都结束了，就表示项目完成了
            if (finishedCount == planList.size() && finishedCount != 0)
                project.setFinished(1);
            else
                project.setFinished(0);
        }
        projectMapper.SetOverdueAndFinished(projects);
        return projects;
    }

    /**
     * 删除项目信息
     * id string->int
     * @param id
     */
    public void DeleteProject(String id){
        //查询project id下关联的plan
        List<Plan> plans = planMapper.GetPlansByProjectId(Integer.parseInt(id));
        for (int i=0; i<plans.size(); i++){
            //查询plan id下包含的execute
            List<Execute> executeList = executeMapper.GetExecutesByPlanId(Integer.parseInt(plans.get(i).getId()));
            for (int j=0; j<executeList.size(); j++){
                //删除查询到的execute
                System.out.println("执行删除exe exeid=" + executeList.get(j).getId());
                executeMapper.DeleteExecuteById(Integer.parseInt(executeList.get(j).getId()));
            }
            //删除plan
            System.out.println("执行删除plan planid=" + plans.get(i).getId());
            planMapper.DeletePlanById(Integer.parseInt(plans.get(i).getId()));
        }
        //删除project
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

    /**
     * 通过年份查询该年有多少个项目
     * @param year
     * @return
     */
    public int GetProjectCountByYear(String year){
        return projectMapper.GetProjectCountByYear(year);
    }

    /**
     * 得到总数
     * @return
     */
    public int GetAllCountOfProject(){
        return projectMapper.GetAllCountOfProject();
    }
}
