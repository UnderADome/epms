package com.wisdri.epms.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        return "page/content";
    }
}
