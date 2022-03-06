package com.wisdri.epms.Controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.wisdri.epms.Entity.Execute;
import com.wisdri.epms.Entity.Plan;
import com.wisdri.epms.Entity.Project;
import com.wisdri.epms.Entity.Receive.SearchPlan;
import com.wisdri.epms.Service.ExecuteService;
import com.wisdri.epms.Service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PlanController {

    @Autowired
    public PlanService planService;

    @RequestMapping(value="epmsview/ReadPlanInfo")
    public ModelAndView ShowReadPlanInfo(@RequestParam(required = false) String projectId){
        ModelAndView view = new ModelAndView();
        if (projectId != null && (!projectId.equals(""))){
            System.out.println("通过project id查找计划");
            //查询
            List<Plan> plans = planService.GetPlansByProjectId(projectId);
            view.addObject("plans", plans);
        }
        view.setViewName("epmsview/plan/ReadPlanInfo");
        return view;
    }
    @RequestMapping(value="epmsview/AddPlan")
    public ModelAndView ShowAddPlan(@RequestParam String projectId){
        ModelAndView view = new ModelAndView();
        view.addObject("projectId", projectId);
        view.setViewName("epmsview/plan/AddPlan.html");
        return view;
    }
    @RequestMapping(value="epmsview/EditPlan")
    public ModelAndView ShowEditPlanContent(@RequestParam String id){
        System.out.println("显示Edit画面：" + id);
        ModelAndView view = new ModelAndView();
        //查询
        Plan plan = planService.GetPlanById(Integer.parseInt(id));
        view.addObject(plan);
        view.setViewName("epmsview/plan/EditPlan.html");
        return view;
    }

    /**
     * 提交计划的具体内容
     * @param plan
     */
    @RequestMapping(value="plan/SubmitPlan", method = RequestMethod.POST)
    @ResponseBody
    public String SubmitPlan(@RequestBody Plan plan){
        try{
            planService.SavePlan(plan);
        }
        catch (Exception e){
            e.printStackTrace();
            return "0";
        }
        return "1";  //返回1表示成功
    }

    /**
     * 获取计划信息
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value="plan/ReadPlanInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> ReadPlanInfo(@RequestParam("page") String page, @RequestParam("limit") String limit,
                                            @RequestParam(value = "plan",required = false) String plan){
        System.out.println("查询计划基本信息");
        SearchPlan plan1 = JSON.parseObject(plan, SearchPlan.class);

        Page<Plan> mResultList = new Page<Plan>();
        if (plan1 == null)
            mResultList = planService.GetPlanByPage(Integer.parseInt(page), Integer.parseInt(limit));
        else
            mResultList = planService.GetPlanByPageAndCondition(Integer.parseInt(page), Integer.parseInt(limit), plan1);
        //按照layui需要的标准格式进行封装
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "消息传递成功");
        map.put("count", planService.GetAllCountOfPlan());
        map.put("data", mResultList);

        return  map;
    }

    /**
     * 删除计划信息
     * 成功返回1 失败返回0
     * @return
     */
    @RequestMapping(value="plan/DeletePlan", method = RequestMethod.POST)
    @ResponseBody
    public void DeletePlan(@RequestParam String id){  //并非一定要指定@RequestParam的详细参数
        System.out.println("执行删除函数, 获取的id："+id);
        planService.DeletePlan(id);
    }

    /**
     * 更新计划内容
     * @param plan
     * @return
     */
    @RequestMapping(value="plan/UpdatePlan", method = RequestMethod.POST)
    @ResponseBody
    public String UpdatePlan(@RequestBody Plan plan){
        try{
            planService.UpdatePlan(plan);
        }
        catch (Exception e){
            e.printStackTrace();
            return "0";
        }
        return "1";  //返回1表示成功
    }

    /**
     * 表示当前的计划已完成
     * @param id
     * @return
     */
    @RequestMapping(value = "plan/FinishPlan", method = RequestMethod.POST)
    @ResponseBody
    public String FinishPlan(@RequestParam String id){
        return planService.FinishPlan(id);
    }
}
