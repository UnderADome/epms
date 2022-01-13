package com.wisdri.epms.Controller;

import com.wisdri.epms.Entity.TTest;
import com.wisdri.epms.Entity.WangEditor;
import com.wisdri.epms.ResultEntity.MeetingResult;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class AppController {

    //@RequestMapping("/")
    @ResponseBody
    public String App(){
        return "123";
    }

    @RequestMapping("/")
    public String app(){
        return "index.html";
    }

    @RequestMapping("/page/welcome-1.html")
    public String test(){
        return "/page/welcome-1";
    }

    @RequestMapping("page/{content}")
    public String test1(@PathVariable("content") String content){
        System.out.println(content);
        return "page/"+content;
    }

    @RequestMapping("project/{content1}")
    public String test2(@PathVariable("content1") String content1){
        System.out.println(content1);
        return "project/"+content1;
    }

    @RequestMapping(value = "test/submit", method = RequestMethod.POST)
    @ResponseBody
    public WangEditor UploadOnePicture(@RequestParam("pictures") MultipartFile multipartFile, HttpServletRequest request){
        System.out.println("图片上传功能开始");
        try {
            //拿到图片
            byte[] bytes = multipartFile.getBytes();
            //设置图片的存储位置
            String path = "E:\\epms\\pictures\\";
//            path = ResourceUtils.getURL("static").getPath() + "/pictures/";
            //path = ClassUtils.getDefaultClassLoader().getResource("static/pictures").getPath();
            //path = request.getSession().getServletContext().getRealPath("static")+ "/pictures/";
            path = ResourceUtils.getURL("classpath:static").getPath() + "/pictures/";
            System.out.println(path);
            path = URLDecoder.decode(path,"utf-8");
            System.out.println(path);
            File imgFile = new File(path);
            if (!imgFile.exists()) {
                imgFile.mkdirs();
            }
            String fileName = multipartFile.getOriginalFilename();// 文件名称
            System.out.println(path + fileName);

            try (FileOutputStream fos = new FileOutputStream(new File(path + fileName))) {
                int len = 0;
                fos.write(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String value = "http://localhost:8080/pictures/" + fileName;
            String[] values = { value };

            WangEditor wangEditor = new WangEditor();
            wangEditor.setCode(0);
            wangEditor.setMsg("上传成功");
            wangEditor.setData(values);

            System.out.println(wangEditor.toString());
            return wangEditor;
        }catch (Exception e){
            return null;
        }
    }

    @RequestMapping(value = "test/submitMulti", method = RequestMethod.POST)
    @ResponseBody
    public WangEditor UploadMultiPicture(@RequestParam("pictures") MultipartFile[] multipartFiles, HttpServletRequest request){
        System.out.println("图片上传功能开始");
        ArrayList<String> values = new ArrayList<String>();
        for (int i=0; i< multipartFiles.length; i++){
            System.out.println(multipartFiles[i].getName());
            try {
                //拿到图片
                byte[] bytes = multipartFiles[i].getBytes();
                //设置图片的存储位置
                String path = ResourceUtils.getURL("classpath:static").getPath() + "/pictures/";
                System.out.println(path);
                path = URLDecoder.decode(path,"utf-8");
                System.out.println(path);
                File imgFile = new File(path);
                if (!imgFile.exists()) {
                    imgFile.mkdirs();
                }
                String fileName = multipartFiles[i].getOriginalFilename();// 文件名称
                System.out.println(path + fileName);

                try (FileOutputStream fos = new FileOutputStream(new File(path + fileName))) {
                    int len = 0;
                    fos.write(bytes);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String value = "http://localhost:8080/pictures/" + fileName;
                values.add(value);

            }catch (Exception e){
                //return null;  //似乎没有什么必要
            }
        }
        WangEditor wangEditor = new WangEditor();
        wangEditor.setCode(0);
        wangEditor.setMsg("上传成功");
        wangEditor.setData(values.toArray(new String[values.size()]));

        System.out.println(wangEditor.toString());
        return wangEditor;
    }


    @RequestMapping(value="test/submithtml", method = RequestMethod.POST)
    @ResponseBody
    public void test4(@RequestBody TTest ddd){
        System.out.println(ddd.getText() + " " +ddd.getTitle() + "  " + ddd.getEditortext());
    }

    @RequestMapping(value="test/ReadMeetingInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> ReadMeetingInfo(@RequestParam("page") String page, @RequestParam("limit") String limit){
        System.out.println("查询会议基本信息");

        MeetingResult mResult = new MeetingResult();
        mResult.id = "111111111111111111111111111111111111111111";
        mResult.content = "111111111111111111111111111111111111111";
        mResult.meetingTime = LocalDateTime.now();
        mResult.infoCreateTime = LocalDateTime.now();
        mResult.organize = "1111111111111111111111111111111111111";
        mResult.partner = "12312312312312312312312312312312313";
        mResult.question = "12312312123123123123123123123123";
        mResult.type = "23412312312312312312312312312";

        ArrayList<MeetingResult> mResultList = new ArrayList<MeetingResult>();
        mResultList.add(mResult);
        mResultList.add(mResult);

        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "消息传递成功");
        map.put("count", "100");
        map.put("data", mResultList);

        return  map;
    }
}
