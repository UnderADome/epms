package com.wisdri.epms.Controller;

import com.wisdri.epms.ResultEntity.WangEditor;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.util.*;

@Controller
public class AppController {

//    @RequestMapping("/")
//    public String app(){
//        return "epmsview/project/ReadProjectInfo";
//    }

    @RequestMapping("page/{content}")
    public String test1(@PathVariable("content") String content){
        System.out.println(content);
        return "page/"+content;
    }

//    @RequestMapping("project/{content1}")
//    public String test2(@PathVariable("content1") String content1){
//        System.out.println(content1);
//        return "project/"+content1;
//    }

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

    /**
     * 批量存储从富文本中上传的图片
     * @param multipartFiles
     * @param request
     * @return
     */
    @RequestMapping(value = "wangEditor/submitMulti", method = RequestMethod.POST)
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
                //处理成UTF8的URL
                path = URLDecoder.decode(path,"utf-8");
                File imgFile = new File(path);
                if (!imgFile.exists()) {
                    imgFile.mkdirs();
                }
                String fileName = multipartFiles[i].getOriginalFilename();// 文件名称
                //可以不用传递到数据库中，在富文本中就已经存在了图片的URL
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

}
