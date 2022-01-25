package com.wisdri.epms.Controller;

import com.wisdri.epms.Entity.Execute;
import com.wisdri.epms.Entity.Plan;
import com.wisdri.epms.Entity.Project;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;

@Controller
public class TimelineController {
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


}
