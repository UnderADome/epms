package com.wisdri.epms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 李韬 @date 2022/4/7 15:56
 * @description 实现WebMvcConfigurer接口，注册拦截器
 */
@Configuration
public class LoginConfig implements WebMvcConfigurer{

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册拦截器
        InterceptorRegistration registration = registry.addInterceptor(new UserLoginInterceptor());
        registration.addPathPatterns(
                "/epmsview/**",
                "/project/**",
                "/",
                "/index");
        registration.excludePathPatterns(
                "/login",
                //"/login/**",
                "/checklogin",
                "/**/*.js",                  //js静态资源
                "/**/*.css",                  //css静态资源
                "**/api/**",
                "**/css/**",
                "**/images/**",
                "**/js/**",
                "**/lib/**",
                "**/static/**"
        ); //增加排除项，主要是静态资源
    }
}
