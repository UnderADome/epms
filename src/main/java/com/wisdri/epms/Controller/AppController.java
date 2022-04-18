package com.wisdri.epms.Controller;

import com.wisdri.epms.Dao.CommonMapper;
import com.wisdri.epms.Entity.Operation;
import com.wisdri.epms.Entity.Person;
import com.wisdri.epms.ResultEntity.WangEditor;
import com.wisdri.epms.Service.LoginService;
import com.wisdri.epms.Util.LogUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@Api("测试")
@Slf4j
public class AppController {
    //region 测试用
    /**
     * 显示layuimini模板中自带的内容
     * @param content
     * @return
     */
    @RequestMapping("page/{content}")
    public String test1(@PathVariable("content") String content){
        System.out.println(content);
        return "page/"+content;
    }

    /**
     * 测试上传一张图片
     * @param multipartFile
     * @param request
     * @return
     */
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
    //endregion

    //region 用户登录拦截
    @Autowired
    LoginService loginService;
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    LogUtil logUtil;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index(Model model, HttpServletRequest request) {
        //Person person = (Person) request.getSession().getAttribute("user");
        String person = (String)request.getSession().getAttribute("user");
        model.addAttribute("user", person);
        log.info(person + "已进入");
        //return "redirect:#/epmsview/ReadProjectInfo";
        //return "#/epmsview/Project/ReadProjectInfo.html";
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:#/epmsview/ReadProjectInfo");  //这里的跳转一定要加上#，否则不能成功
        return modelAndView;
    }

    /**
     * get方式用来进入login页面
     * @return
     */
    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String loginIndex() {
        log.info("尝试进入login页面");
        return "epmsview/login";
    }

    /**
     * post方式用来验证
     * @param id
     * @param password
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = {"/checklogin"}, method = RequestMethod.POST)
    public ModelAndView Login(@RequestParam(name = "id")String id, @RequestParam(name = "password")String password,
                        Model model, HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        if (!id.equals("") && !password.equals("")) {
            log.info("执行用户名密码检测 " + id + " " + password);
            //调用Service中的方法
            if (loginService.CheckPasswordFromDB(id, password) == true){
                model.addAttribute("userid", id);
                request.getSession().setAttribute("userid", id);
                view.setViewName("redirect:/index");
                Operation operation = logUtil.CreateOneLog(id, request.getRemoteAddr(), "用户登录-数据库验证", LocalDateTime.now().toString(), 1);
                commonMapper.SaveLog(operation);
            }
            else if (loginService.CheckPasswordFromLDAP(id, password) == true){
                model.addAttribute("userid", id);
                request.getSession().setAttribute("userid", id);
                view.setViewName("redirect:/index");
                Operation operation = logUtil.CreateOneLog(id, request.getRemoteAddr(), "用户登录-LDAP验证", LocalDateTime.now().toString(), 1);
                commonMapper.SaveLog(operation);
            }
            else{
                view.addObject("loginFlag", "false");
                view.setViewName("epmsview/login");
                Operation operation = logUtil.CreateOneLog(id, request.getRemoteAddr(), "用户登录-验证失败", LocalDateTime.now().toString(), 0);
                commonMapper.SaveLog(operation);
            }
        }
        else{
            view.addObject("loginFlag", "false");
            view.setViewName("epmsview/login");
            Operation operation = logUtil.CreateOneLog(id, request.getRemoteAddr(), "用户登录-输入有误，返回登录页面", LocalDateTime.now().toString(), 0);
            commonMapper.SaveLog(operation);
        }
        return view;
    }

    //endregion
}
