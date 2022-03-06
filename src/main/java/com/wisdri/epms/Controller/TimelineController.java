package com.wisdri.epms.Controller;

import com.wisdri.epms.Dao.ExecuteMapper;
import com.wisdri.epms.Entity.Execute;
import com.wisdri.epms.Entity.Plan;
import com.wisdri.epms.Entity.Project;
import com.wisdri.epms.ResultEntity.AnnualProject;
import com.wisdri.epms.ResultEntity.MonthInfo;
import com.wisdri.epms.ResultEntity.ProjectResult;
import com.wisdri.epms.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class TimelineController {

    @Autowired
    TimelineService timelineService;
    @Autowired
    ProjectService projectService;
    @Autowired
    MeetingService meetingService;
    @Autowired
    TrainService trainService;
    @Autowired
    ExecuteService executeService;

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
    public ModelAndView ShowAccomplish(@RequestParam(value = "id",required = false) String id){
        ModelAndView view = new ModelAndView();
        if (id != null){
            view.addObject("PlanAndExes", timelineService.GetPlanAndExesByProjectId(id));
            view.addObject("projectName", projectService.GetProjectById(Integer.parseInt(id)).getName());
        }
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

    @RequestMapping("ShowAnnualProSum")
    public String ShowAnnualProSum(){
        return "epmsview/board/AnnualProSum";
    }

    @RequestMapping("epmsview/AnnualProSum")
    public String ShowAnnualProSum1(){
        return "epmsview/board/AnnualProSum";
    }

    @RequestMapping(value = "GetAnnualInfo", method = RequestMethod.POST)
    @ResponseBody
    public AnnualProject GetAnnualInfo(String year){
        AnnualProject annualProject = new AnnualProject();
        //查询项目信息
        //在查询的过程中对项目中完成、未完成；逾期、未逾期的数据进行重计算
        int projectNum = projectService.GetProjectCountByYear(year);
        int executeNum = executeService.GetExecuteCountByYear(year);

        //查询执行信息
        List<MonthInfo> executeMonthInfo = executeService.GetExecuteCountByYearMonth(year);

        //查询会议信息
        List<MonthInfo> meetingMonthInfo = meetingService.GetMeetingCountByYearMonth(year);
        int meetingNum = meetingMonthInfo.size();
        for (MonthInfo monthInfo : meetingMonthInfo){
            meetingNum += monthInfo.getMeetingCount();
        }

        //查询培训信息
        List<MonthInfo> trainMonthInfo = trainService.GetTrainCountByYearMonth(year);
        int trainNum = 0;//trainMonthInfo.size();
        for (MonthInfo monthInfo : trainMonthInfo){
            trainNum += monthInfo.getTrainCount();
        }

        //查询年度实施最多的一个月
        String exeMostMonth = executeService.GetExeMostMonthByYear(year);

        //对会议、培训等信息进行整合
        List<MonthInfo> monthInfoList = new ArrayList<MonthInfo>();
        for (int month = 1; month <=12; month++){
            MonthInfo monthInfo = new MonthInfo();
            monthInfo.setTime(month + "");

            for (int i=0; i<meetingMonthInfo.size(); i++){
                if (month == Integer.parseInt(meetingMonthInfo.get(i).getTime())){
                    monthInfo.setMeetingCount(meetingMonthInfo.get(i).getMeetingCount());
                }
            }
            for (int i=0; i<trainMonthInfo.size(); i++){
                if (month == Integer.parseInt(trainMonthInfo.get(i).getTime())){
                    monthInfo.setTrainCount(trainMonthInfo.get(i).getTrainCount());
                }
            }
            for (int i=0; i<executeMonthInfo.size(); i++){
                if (month == Integer.parseInt(executeMonthInfo.get(i).getTime())){
                    monthInfo.setExecuteCount(executeMonthInfo.get(i).getExecuteCount());
                }
            }

            monthInfoList.add(monthInfo);
        }
        annualProject.setProjectNum(projectNum);
        annualProject.setExecuteNum(executeNum);
        annualProject.setMeetingNum(meetingNum);
        annualProject.setTrainNum(trainNum);
        annualProject.setExeMostMonth(exeMostMonth);
        annualProject.setMonthInfoList(monthInfoList);
        return annualProject;
    }

}
