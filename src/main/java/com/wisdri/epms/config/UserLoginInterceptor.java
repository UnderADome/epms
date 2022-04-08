package com.wisdri.epms.config;

import com.wisdri.epms.Entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author 李韬 @date 2022/4/7 15:48
 * @description 登录拦截器
 */
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {
    /**
     * 在请求处理之前调用（Controller之前调用）
     * @param request
     * @param response
     * @param handler
     * @return 将用户信息写入到session中 /or/ 跳转到登录页面
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("执行用户登录拦截！");
        try {
            HttpSession session = request.getSession();
            //统一拦截（查询当前session是否存在person）(这里person会在每次登录成功后，写入session)
            //Person person = (Person) session.getAttribute("person");
            String person = (String)request.getSession().getAttribute("user");
            if (person != null && !person.equals("")) {
                log.info("已获取session - " + person + " sessionid = " + request.getSession().getAttribute("user"));
                return true;
            }  //else
            log.info("未获取到session，跳转到login页面");
            response.sendRedirect("/login");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
        //如果设置为false时，被请求时，拦截器执行到此处将不会继续操作
        //如果设置为true时，请求将会继续执行后面的操作
    }

    /**
     * 请求处理之后进行调用，但是在试图被渲染之前（Controller之后调用）
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 整个请求结束之后被调用，也就是在DispatchServlet渲染了对应的视图之后执行（主要用于进行资源清理工作）
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
