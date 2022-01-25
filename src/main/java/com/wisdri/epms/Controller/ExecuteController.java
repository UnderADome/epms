package com.wisdri.epms.Controller;

import com.github.pagehelper.Page;
import com.wisdri.epms.Entity.Execute;
import com.wisdri.epms.Service.ExecuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ExecuteController {

    @Autowired
    public ExecuteService executeService;

    @RequestMapping(value="epmsview/ReadExecuteInfo")
    public String ShowReadExecuteInfo(){
        return "epmsview/execute/ReadExecuteInfo";
    }
    @RequestMapping(value="epmsview/AddExecute")
    public ModelAndView ShowAddExecute(@RequestParam String planId){
        ModelAndView view = new ModelAndView();
        view.addObject("planId", planId);
        view.setViewName("epmsview/execute/AddExecute.html");
        return view;
    }
    @RequestMapping(value="epmsview/EditExecute")
    public ModelAndView ShowEditExecuteContent(@RequestParam String id){
        System.out.println("显示Edit画面：" + id);
        ModelAndView view = new ModelAndView();
        //查询
        Execute execute = executeService.GetExecuteById(Integer.parseInt(id));
        view.addObject(execute);
        view.setViewName("epmsview/execute/EditExecute.html");
        return view;
    }

    /**
     * 提交实施的具体内容
     * @param execute
     */
    @RequestMapping(value="execute/SubmitExecute", method = RequestMethod.POST)
    @ResponseBody
    public String SubmitExecute(@RequestBody Execute execute){
        try{
            executeService.SaveExecute(execute);
        }
        catch (Exception e){
            e.printStackTrace();
            return "0";
        }
        return "1";  //返回1表示成功
    }

    /**
     * 获取实施信息
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value="execute/ReadExecuteInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> ReadExecuteInfo(@RequestParam("page") String page, @RequestParam("limit") String limit){
        System.out.println("查询实施基本信息");

        Page<Execute> mResultList = executeService.GetExecuteByPage(Integer.parseInt(page), Integer.parseInt(limit));
        //按照layui需要的标准格式进行封装
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "消息传递成功");
        map.put("count", "100");
        map.put("data", mResultList);

        return  map;
    }

    /**
     * 删除实施信息
     * 成功返回1 失败返回0
     * @return
     */
    @RequestMapping(value="execute/DeleteExecute", method = RequestMethod.POST)
    @ResponseBody
    public void DeleteExecute(@RequestParam String id){  //并非一定要指定@RequestParam的详细参数
        System.out.println("执行删除函数, 获取的id："+id);
        executeService.DeleteExecute(id);
    }

    /**
     * 更新实施内容
     * @param execute
     * @return
     */
    @RequestMapping(value="execute/UpdateExecute", method = RequestMethod.POST)
    @ResponseBody
    public String UpdateExecute(@RequestBody Execute execute){
        try{
            executeService.UpdateExecute(execute);
        }
        catch (Exception e){
            e.printStackTrace();
            return "0";
        }
        return "1";  //返回1表示成功
    }

    /**
     * 表示当前的计实施已完成
     * @param id
     * @return
     */
    @RequestMapping(value = "execute/FinishExecute", method = RequestMethod.POST)
    @ResponseBody
    public String FinishExecute(@RequestParam String id){
        return executeService.FinishExecute(id);
    }
}
