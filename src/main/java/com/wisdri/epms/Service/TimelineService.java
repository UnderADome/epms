package com.wisdri.epms.Service;

import com.wisdri.epms.Dao.ExecuteMapper;
import com.wisdri.epms.Dao.PlanMapper;
import com.wisdri.epms.Dao.ProjectMapper;
import com.wisdri.epms.Entity.Execute;
import com.wisdri.epms.Entity.Plan;
import com.wisdri.epms.Entity.Project;
import com.wisdri.epms.ResultEntity.ProjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class TimelineService {

    @Autowired
    PlanMapper planMapper;
    @Autowired
    ExecuteMapper executeMapper;
    @Autowired
    ProjectMapper projectMapper;

//    public HashMap<Plan, List<Execute>> GetProjectDetailsById(@RequestParam int id){
//        HashMap<Plan, List<Execute>> projectDetails = new HashMap<Plan, List<Execute>>();
//        Plan plan = service;
//        List<Execute> exes = service;
//        projectDetails.put(plan, exes);
//    }

    /**
     * 通过project id去查找底下对应的计划以及计划对应的实施
     * @param projectName project-name
     * @return
     */
    public ArrayList<ProjectResult> GetPlanAndExesByProjectName(String projectName){
        ArrayList<ProjectResult> result = new ArrayList<ProjectResult>();
        //先通过项目名称去做匹配项目的名称
        int projectid = 0;
        try{
            projectid = projectMapper.FindProjectIdByName(projectName);
            System.out.println("projectid=" + projectid);
        }catch (Exception e){
            System.out.println("查询project失败");
            e.printStackTrace();
            return null;
        }

        //先通过project id查找对应的计划
        List<Plan> plans = planMapper.GetPlansByProjectId(projectid);
        //根据得到的计划，分别得到计划相关的实施，一起放到map中
        for (int i=0; i<plans.size(); i++){
            ProjectResult projectResult = new ProjectResult();
            projectResult.setName(plans.get(i).getName());
            projectResult.setContent(plans.get(i).getContent());
            projectResult.setDone(plans.get(i).getFinished());
            projectResult.setStartTime(plans.get(i).getPlanStartTime());
            projectResult.setEndTime(plans.get(i).getPlanEndTime());
            projectResult.setItemType(1);
            result.add(projectResult);
            List<Execute> executes = executeMapper.GetExecutesByPlanId(Integer.parseInt(plans.get(i).getId()));
            for (int j=0; j<executes.size(); j++){
                projectResult = new ProjectResult();
                projectResult.setName("");//executes.get(j).getName()
                projectResult.setContent(executes.get(j).getContent());
                projectResult.setDone(executes.get(j).getFinished());
                projectResult.setStartTime(executes.get(j).getExeStartTime());
                projectResult.setEndTime(executes.get(j).getExeEndTime());
                projectResult.setItemType(2);
                result.add(projectResult);
            }
        }


        for (int i=0; i<result.size();i++){
            System.out.print(result.get(i).getContent() + " ");
            System.out.print(result.get(i).getStartTime() + " ");
            System.out.print(result.get(i).getEndTime() + " ");
            System.out.print(result.get(i).getDone() + " ");
            System.out.print(result.get(i).getItemType() + " ");
            System.out.print(result.get(i).getName() + " ");
            System.out.println();
        }

        return result;
    }

    public Project FindProjectByProjectName(String projectName){
        return projectMapper.FindProjectByProjectName(projectName);
    }

    /**
     * 通过project id去查找底下对应的计划以及计划对应的实施
     * @param id project-id
     * @return
     */
    public ArrayList<ProjectResult> GetPlanAndExesByProjectId(String id){
        ArrayList<ProjectResult> result = new ArrayList<ProjectResult>();
        //先通过项目名称去做匹配项目的名称
        int projectid = Integer.parseInt(id);

        //先通过project id查找对应的计划
        List<Plan> plans = planMapper.GetPlansByProjectId(projectid);
        //根据得到的计划，分别得到计划相关的实施，一起放到map中
        for (int i=0; i<plans.size(); i++){
            ProjectResult projectResult = new ProjectResult();
            projectResult.setName(plans.get(i).getName());
            projectResult.setContent(plans.get(i).getContent());
            projectResult.setDone(plans.get(i).getFinished());
            projectResult.setStartTime(plans.get(i).getPlanStartTime());
            projectResult.setEndTime(plans.get(i).getPlanEndTime());
            projectResult.setItemType(1);
            result.add(projectResult);
            List<Execute> executes = executeMapper.GetExecutesByPlanId(Integer.parseInt(plans.get(i).getId()));
            for (int j=0; j<executes.size(); j++){
                projectResult = new ProjectResult();
                projectResult.setName("");//executes.get(j).getName()
                projectResult.setContent(executes.get(j).getContent());
                projectResult.setDone(executes.get(j).getFinished());
                projectResult.setStartTime(executes.get(j).getExeStartTime());
                projectResult.setEndTime(executes.get(j).getExeEndTime());
                projectResult.setItemType(2);
                result.add(projectResult);
            }
        }


        for (int i=0; i<result.size();i++){
            System.out.print(result.get(i).getContent() + " ");
            System.out.print(result.get(i).getStartTime() + " ");
            System.out.print(result.get(i).getEndTime() + " ");
            System.out.print(result.get(i).getDone() + " ");
            System.out.print(result.get(i).getItemType() + " ");
            System.out.print(result.get(i).getName() + " ");
            System.out.println();
        }

        return result;
    }

}
