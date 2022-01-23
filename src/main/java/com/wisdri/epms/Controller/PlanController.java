package com.wisdri.epms.Controller;

import com.github.pagehelper.Page;
import com.wisdri.epms.Entity.Execute;
import com.wisdri.epms.Entity.Plan;
import com.wisdri.epms.Service.ExecuteService;
import com.wisdri.epms.Service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PlanController {

    @Autowired
    public PlanService planService;

    @RequestMapping(value="epmsview/ReadPlanInfo")
    public String ShowReadPlanInfo(){
        return "epmsview/plan/ReadPlanInfo";
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
    public Map<String, Object> ReadPlanInfo(@RequestParam("page") String page, @RequestParam("limit") String limit){
        System.out.println("查询计划基本信息");

        Page<Plan> mResultList = planService.GetPlanByPage(Integer.parseInt(page), Integer.parseInt(limit));
        //按照layui需要的标准格式进行封装
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "消息传递成功");
        map.put("count", "100");
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

}
