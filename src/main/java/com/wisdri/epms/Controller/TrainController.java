package com.wisdri.epms.Controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.wisdri.epms.Entity.Meeting;
import com.wisdri.epms.Entity.Train;
import com.wisdri.epms.Service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TrainController {

    @Autowired
    public TrainService trainService;

    @RequestMapping(value="epmsview/ReadTrainInfo")
    public String ShowReadTrainInfo(){
        return "epmsview/train/ReadTrainInfo";
    }
    @RequestMapping(value="epmsview/AddTrain")
    public String ShowAddTrain(){
        return "epmsview/train/AddTrain";
    }
    @RequestMapping(value="epmsview/EditTrain")
    public ModelAndView ShowEditTrainContent(@RequestParam String id){
        System.out.println("显示Edit画面：" + id);
        ModelAndView view = new ModelAndView();
        //查询
        Train train = trainService.GetTrainById(Integer.parseInt(id));
        System.out.println(train.trainTime);
        view.addObject(train);
        view.setViewName("epmsview/train/EditTrain.html");
        return view;
    }

    /**
     * 提交培训的具体内容
     * @param train
     */
    @RequestMapping(value="train/SubmitTrain", method = RequestMethod.POST)
    @ResponseBody
    public String SubmitTrain(@RequestBody Train train){
        System.out.println(train.trainTime + " " + train.content);
        try{
            trainService.SaveTrain(train);
        }
        catch (Exception e){
            e.printStackTrace();
            return "0";
        }
        return "1";  //返回1表示成功
    }

    /**
     * 获取培训信息
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value="train/ReadTrainInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> ReadTrainInfo(@RequestParam("page") String page, @RequestParam("limit") String limit,
                                             @RequestParam(value = "train", required = false)String train){
        System.out.println("查询培训基本信息");
        Train train1 = JSON.parseObject(train, Train.class);
        Page<Train> mResultList = new Page<Train>();
        if (train1 == null)
            mResultList = trainService.GetTrainByPage(Integer.parseInt(page), Integer.parseInt(limit));
        else
            mResultList = trainService.GetTrainByPageAndCondition(Integer.parseInt(page), Integer.parseInt(limit), train1);
        //按照layui需要的标准格式进行封装
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "消息传递成功");
        map.put("count", mResultList.size());
        map.put("data", mResultList);

        return  map;
    }

    /**
     * 删除培训信息
     * 成功返回1 失败返回0
     * @return
     */
    @RequestMapping(value="train/DeleteTrain", method = RequestMethod.POST)
    @ResponseBody
    public void DeleteTrain(@RequestParam String id){  //并非一定要指定@RequestParam的详细参数
        System.out.println("执行删除函数, 获取的id："+id);
        trainService.DeleteTrain(id);
    }

    /**
     * 更新培训内容
     * @param train
     * @return
     */
    @RequestMapping(value="train/UpdateTrain", method = RequestMethod.POST)
    @ResponseBody
    public String UpdateTrain(@RequestBody Train train){
        try{
            trainService.UpdateTrain(train);
        }
        catch (Exception e){
            e.printStackTrace();
            return "0";
        }
        return "1";  //返回1表示成功
    }
}
