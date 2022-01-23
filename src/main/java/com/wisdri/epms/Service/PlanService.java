package com.wisdri.epms.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wisdri.epms.Dao.PlanMapper;
import com.wisdri.epms.Entity.Plan;
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
}
