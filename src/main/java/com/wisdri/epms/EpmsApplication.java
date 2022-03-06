package com.wisdri.epms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class EpmsApplication //extends SpringBootServletInitializer
{

	public static void main(String[] args) {
		SpringApplication.run(EpmsApplication.class, args);
	}

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		//参数为当前SpringBoot启动类
//		//构造新资源
//		return builder.sources(EpmsApplication.class);
//	}
}
