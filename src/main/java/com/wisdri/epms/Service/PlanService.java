package com.wisdri.epms.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wisdri.epms.Dao.ExecuteMapper;
import com.wisdri.epms.Dao.PlanMapper;
import com.wisdri.epms.Entity.Execute;
import com.wisdri.epms.Entity.Plan;
import com.wisdri.epms.Entity.Receive.SearchPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlanService {
    @Autowired
    private PlanMapper planMapper;
    @Autowired
    private ExecuteMapper executeMapper;

    /**
     * 存储计划信息
     * @param plan
     */
    public void SavePlan(Plan plan){
        planMapper.SavePlan(plan);
    }

    /**
     * 分页查询计划相关信息
     * @param page
     * @param pagesize
     * @return
     */
    public Page<Plan> GetPlanByPage(int page, int pagesize){
        PageHelper.startPage(page, pagesize);
        Page<Plan> plans = planMapper.GetPlanByPage();
        plans = CalIfOverdue(plans);
        return plans;
    }

    public Page<Plan> GetPlanByPageAndCondition(int page, int pagesize, SearchPlan plan){
        PageHelper.startPage(page, pagesize);
        //分三种情况：
        //1、只对项目名称等字符串进行匹配
        if (plan.getPlanStartTime() == null && plan.getPlanEndTime() == null){
            Page<Plan> plans = planMapper.GetPlanByPageAndNormalCondition(plan);
            plans = CalIfOverdue(plans);
            return plans;
        }
        //2、有一个时间，直接匹配
        if ((plan.getPlanStartTime() == null && plan.getPlanEndTime() != null) ||
                (plan.getPlanStartTime() != null && plan.getPlanEndTime() == null)){
            //调用的方法同上面的
            Page<Plan> plans = planMapper.GetPlanByPageAndNormalCondition(plan);
            plans = CalIfOverdue(plans);
            return plans;
        }
        //3、有两个时间，范围查询
        else { //都不为null，就是时间范围查询
            Page<Plan> plans = planMapper.GetPlanByPageAndTimeRange(plan);
            plans = CalIfOverdue(plans);
            return plans;
        }
    }

    public Page<Plan> CalIfOverdue(Page<Plan> plans){
        for (Plan plan : plans){
            //根据plan id查询executes
            List<Execute> executeList = executeMapper.GetExecutesByPlanId(Integer.parseInt(plan.getId()));
            //表示有多少个实施完成了
            int finishedCount = 0;
            for (int i=0; i<executeList.size(); i++){
                //阶段（计划）中有一个实施逾期了，即表示该阶段逾期
//                if (executeList.get(i).getOverdue() == 1)
//                    plan.setOverdue(1);
                //如果一个实施的实际完成时间比预订的阶段时间晚，则表示阶段（计划）逾期
                if (executeList.get(i).getExeRealEndTime() != null && plan.getPlanEndTime() != null)
                if (executeList.get(i).getExeRealEndTime().isAfter(plan.getPlanEndTime()))
                    plan.setOverdue(1);
                //如果实施中有一个是未完成的状态，则表示阶段未完成
                if (executeList.get(i).getFinished() == 0)
                    plan.setFinished(0);
                else
                    finishedCount++;
            }
            //如果所有的实施都结束了，则表示阶段（计划）结束了
            System.out.println(finishedCount + " " + executeList.size());
            if (finishedCount == executeList.size() && finishedCount != 0)
                plan.setFinished(1);
            else
                plan.setFinished(0);
        }
        planMapper.SetOverdueAndFinished(plans);
        return plans;
    }

    /**
     * 删除计划信息
     * id string->int
     * @param id
     */
    public void DeletePlan(String id){
        //查询plan id下包含的execute
        List<Execute> executeList = executeMapper.GetExecutesByPlanId(Integer.parseInt(id));
        for (int j=0; j<executeList.size(); j++){
            //删除查询到的execute
            System.out.println("执行删除exe exeid=" + executeList.get(j).getId());
            executeMapper.DeleteExecuteById(Integer.parseInt(executeList.get(j).getId()));
        }
        //删除plan
        System.out.println("执行删除plan planid=" + Integer.parseInt(id));
        planMapper.DeletePlanById(Integer.parseInt(id));
    }

    /**
     * 查询计划信息 by id
     * @param id
     * @return
     */
    public Plan GetPlanById(int id){
        return planMapper.GetPlanById(id);
    }

    /**
     * 更新计划内容 by id
     * @param plan
     */
    public void UpdatePlan(Plan plan){
        planMapper.UpdatePlan(plan);
    }
    /**
     * 更新已完成状态
     * @param id
     * @return
     */
    public String FinishPlan(String id){
        try{
            planMapper.FinishPlan(Integer.parseInt(id));
            return "1";
        }catch (Exception e){
            return "0";
        }
    }

    public List<Plan> GetPlansByProjectId(String projectId){
        return planMapper.GetPlansByProjectId(Integer.parseInt(projectId));
    }
}
