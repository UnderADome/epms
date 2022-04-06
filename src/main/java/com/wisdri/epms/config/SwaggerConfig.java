package com.wisdri.epms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

/**
 * @author 李韬 @date 2022/4/6 11:28
 * @description Swagger配置文件
 */
@Configuration
@EnableOpenApi  //Swagger3的配置
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
public class SwaggerConfig {
    /**
     * 用于读取配置文件 application.properties 中 swagger 属性是否开启
     */
    @Value("${springfox.documentation.swagger-ui.enabled}")
    Boolean swaggerEnabled;

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                // 是否开启swagger
                .enable(swaggerEnabled)
                .select()
                // 过滤条件，扫描指定路径下的文件
                .apis(RequestHandlerSelectors.basePackage("com.wisdri.epms"))
                // 指定路径处理，PathSelectors.any()代表不过滤任何路径
                //.paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        /*作者信息*/
        Contact contact = new Contact("litao", "", "1354448288@qq.com");
        return new ApiInfo(
                "Spring Boot 集成 Swagger3 测试",
                "Spring Boot 集成 Swagger3 测试接口文档",
                "v1.0",
                "",
                contact,
                "Apache 2.0",
                "",
                new ArrayList()
        );
    }
}
