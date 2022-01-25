package com.wisdri.epms.Controller;

import com.github.pagehelper.Page;
import com.wisdri.epms.Entity.Project;
import com.wisdri.epms.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ProjectController {

    @Autowired
    public ProjectService projectService;

    @RequestMapping(value="epmsview/ReadProjectInfo")
    public String ShowReadProjectInfo(){
        return "epmsview/project/ReadProjectInfo";
    }
    @RequestMapping(value="epmsview/AddProject")
    public String ShowAddProject(){
        return "epmsview/project/AddProject";
    }
    @RequestMapping(value="epmsview/EditProject")
    public ModelAndView ShowEditProjectContent(@RequestParam String id){
        System.out.println("显示Edit画面：" + id);
        ModelAndView view = new ModelAndView();
        //查询
        Project project = projectService.GetProjectById(Integer.parseInt(id));
        view.addObject(project);
        view.setViewName("epmsview/project/EditProject.html");
        return view;
    }

    /**
     * 提交项目的具体内容
     * @param project
     */
    @RequestMapping(value="project/SubmitProject", method = RequestMethod.POST)
    @ResponseBody
    public String SubmitProject(@RequestBody Project project){
        try{
            projectService.SaveProject(project);
        }
        catch (Exception e){
            e.printStackTrace();
            return "0";
        }
        return "1";  //返回1表示成功
    }

    /**
     * 获取项目信息
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value="project/ReadProjectInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> ReadProjectInfo(@RequestParam("page") String page, @RequestParam("limit") String limit){
        System.out.println("查询项目基本信息");

        Page<Project> mResultList = projectService.GetProjectByPage(Integer.parseInt(page), Integer.parseInt(limit));
        //按照layui需要的标准格式进行封装
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "消息传递成功");
        map.put("count", "100");
        map.put("data", mResultList);

        return  map;
    }

    /**
     * 删除项目信息
     * 成功返回1 失败返回0
     * @return
     */
    @RequestMapping(value="project/DeleteProject", method = RequestMethod.POST)
    @ResponseBody
    public void DeleteProject(@RequestParam String id){  //并非一定要指定@RequestParam的详细参数
        System.out.println("执行删除函数, 获取的id："+id);
        projectService.DeleteProject(id);
    }

    /**
     * 更新项目内容
     * @param project
     * @return
     */
    @RequestMapping(value="project/UpdateProject", method = RequestMethod.POST)
    @ResponseBody
    public String UpdateProject(@RequestBody Project project){
        try{
            projectService.UpdateProject(project);
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
    @RequestMapping(value = "project/FinishProject", method = RequestMethod.POST)
    @ResponseBody
    public String FinishProject(@RequestParam String id){
        return projectService.FinishProject(id);
    }
}
