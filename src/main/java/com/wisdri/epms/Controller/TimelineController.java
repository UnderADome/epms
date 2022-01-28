package com.wisdri.epms.Controller;

import com.wisdri.epms.Entity.Execute;
import com.wisdri.epms.Entity.Plan;
import com.wisdri.epms.Entity.Project;
import com.wisdri.epms.ResultEntity.ProjectResult;
import com.wisdri.epms.Service.TimelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class TimelineController {

    @Autowired
    TimelineService timelineService;

    /**
     * 显示时间线的画面
     * @return
     */
    @RequestMapping(value="epmsview/ProjectTimeline")
    public ModelAndView ShowTimeline(){
        //System.out.println("Timeline：id=" + id);
        ModelAndView view = new ModelAndView();
        view.setViewName("epmsview/board/timeline.html");
        return view;
    }

//    @RequestMapping(value = "epmsview/GetProjectDetailsById")
//    public ModelAndView GetProjectDetailsById(@RequestParam int id){
//        ModelAndView view = new ModelAndView();
//        HashMap<Plan, List<Execute>> projectDetails = service;
//        view.addObject(projectDetails);
//        return view;
//    }

    @RequestMapping(value="epmsview/ProjectAccomplish")
    public ModelAndView ShowAccomplish(){
        //System.out.println("Timeline：id=" + id);
        ModelAndView view = new ModelAndView();
        view.setViewName("epmsview/board/accomplish.html");
        return view;
    }

    @RequestMapping(value = "GetPlanAndExesByProjectName", method = RequestMethod.POST)
    @ResponseBody
    public ArrayList<ProjectResult> GetPlanAndExesByProjectName(String projectName){
        System.out.println("查询项目进度表");
        System.out.println("projectName=" + projectName);
        if (projectName.isEmpty())
            return null;
        System.out.println("传递到页面accomplish.html");
        return timelineService.GetPlanAndExesByProjectName(projectName);
    }
//@RequestMapping(value = "GetPlanAndExesByProjectName", method = RequestMethod.POST)
////@ResponseBody
//public String GetPlanAndExesByProjectName(String projectName, RedirectAttributes attr){
//    System.out.println("查询项目进度表");
//    System.out.println("projectName=" + projectName);
//    if (projectName.isEmpty())
//        return null;
//    System.out.println("传递到页面accomplish.html");
//    attr.addFlashAttribute("project", timelineService.GetPlanAndExesByProjectName(projectName));
//    System.out.println("加入参数");
//    return "redirect:/epmsview/board/accomplish";
//    //return view;
//}
//
//@RequestMapping("epmsview/board/accomplish")
//public String test(ArrayList<ProjectResult> project, Model model){
//    System.out.println("跳转");
//        ModelAndView view = new ModelAndView();
//        view.addObject("project", project);
//        view.setViewName("epmsview/board/accomplish.html");
//        model.addAttribute("project", project);
//        //return view;
//        return "epmsview/board/accomplish.html";
//}
    @RequestMapping(value = "timegoing", method = RequestMethod.POST)
    @ResponseBody
    public Project TimeGoing(String projectName){
        //查询project的时间
        //与现在的时间对比
        //
        return timelineService.FindProjectByProjectName(projectName);
    }
}
