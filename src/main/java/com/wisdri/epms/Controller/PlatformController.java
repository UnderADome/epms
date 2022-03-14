package com.wisdri.epms.Controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.wisdri.epms.Entity.Platform;
import com.wisdri.epms.Service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PlatformController {

    @Autowired
    public PlatformService platformService;

    @RequestMapping(value="epmsview/ReadPlatformInfo")
    public String ShowReadPlatformInfo(){
        System.out.println("查询");
        return "epmsview/platform/ReadPlatformInfo";
    }
    @RequestMapping(value="epmsview/AddPlatform")
    public String ShowAddPlatform(){
        return "epmsview/platform/AddPlatform";
    }
    @RequestMapping(value="epmsview/EditPlatform")
    public ModelAndView ShowEditPlatformContent(@RequestParam String id){
        System.out.println("显示Edit画面：" + id);
        ModelAndView view = new ModelAndView();
        //查询
        Platform platform = platformService.GetPlatformById(Integer.parseInt(id));

        view.addObject(platform);
        view.setViewName("epmsview/platform/EditPlatform.html");
        return view;
    }

    /**
     * 提交会议的具体内容
     * @param platform
     */
    @RequestMapping(value="platform/SubmitPlatform", method = RequestMethod.POST)
    @ResponseBody
    public String SubmitPlatform(@RequestBody Platform platform){
        try{
            platformService.SavePlatform(platform);
        }
        catch (Exception e){
            e.printStackTrace();
            return "0";
        }
        return "1";  //返回1表示成功
    }

    /**
     * 获取会议信息
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value="platform/ReadPlatformInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> ReadPlatformInfo(@RequestParam("page") String page, @RequestParam("limit") String limit,
                                               @RequestParam(value = "platform", required = false)String platform){
        Platform platform1 = JSON.parseObject(platform, Platform.class);
        Page<Platform> mResultList = new Page<Platform>();
        if (platform1 == null)
            mResultList = platformService.GetPlatformByPage(Integer.parseInt(page), Integer.parseInt(limit));
        else
            mResultList = platformService.GetPlatformByPageAndCondition(Integer.parseInt(page), Integer.parseInt(limit), platform1);
        //按照layui需要的标准格式进行封装
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "消息传递成功");
        map.put("count", platformService.GetAllCountOfPlatform());
        map.put("data", mResultList);

        return  map;
    }

    /**
     * 删除会议信息
     * 成功返回1 失败返回0
     * @return
     */
    @RequestMapping(value="platform/DeletePlatform", method = RequestMethod.POST)
    @ResponseBody
    public void DeletePlatform(@RequestParam String id){  //并非一定要指定@RequestParam的详细参数
        System.out.println("执行删除函数, 获取的id："+id);
        platformService.DeletePlatform(id);
    }

    /**
     * 更新会议内容
     * @param platform
     * @return
     */
    @RequestMapping(value="platform/UpdatePlatform", method = RequestMethod.POST)
    @ResponseBody
    public String UpdatePlatform(@RequestBody Platform platform){
        System.out.println(platform);
        System.out.println("UpdatePlatform：" + platform.getContent() + "\n" + platform.getPlatRealEndTime() == null);
        try{
            platformService.UpdatePlatform(platform);
        }
        catch (Exception e){
            e.printStackTrace();
            return "0";
        }
        return "1";  //返回1表示成功
    }

    /**
     * 更改为未完成的状态
     * @param id
     * @return
     */
    @RequestMapping(value = "platform/NotdonePlatform", method = RequestMethod.POST)
    @ResponseBody
    public String NotdonePlatform(@RequestParam String id){
        return platformService.NotdonePlatform(id);
    }
}
