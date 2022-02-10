package com.wisdri.epms.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wisdri.epms.Dao.PlanMapper;
import com.wisdri.epms.Entity.Plan;
import com.wisdri.epms.Entity.Receive.SearchPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanService {
    @Autowired
    private PlanMapper planMapper;

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
        return planMapper.GetPlanByPage();
    }

    public Page<Plan> GetPlanByPageAndCondition(int page, int pagesize, SearchPlan plan){
        PageHelper.startPage(page, pagesize);
        //分三种情况：
        //1、只对项目名称等字符串进行匹配
        if (plan.getPlanStartTime() == null && plan.getPlanEndTime() == null)
            return planMapper.GetPlanByPageAndNormalCondition(plan);
        //2、有一个时间，直接匹配
        if ((plan.getPlanStartTime() == null && plan.getPlanEndTime() != null) ||
                (plan.getPlanStartTime() != null && plan.getPlanEndTime() == null))
            //调用的方法同上面的
            return planMapper.GetPlanByPageAndNormalCondition(plan);
            //3、有两个时间，范围查询
        else //都不为null，就是时间范围查询
            return planMapper.GetPlanByPageAndTimeRange(plan);
    }

    /**
     * 删除计划信息
     * id string->int
     * @param id
     */
    public void DeletePlan(String id){
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
}
