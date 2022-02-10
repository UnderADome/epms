package com.wisdri.epms.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author 李韬 @date 2020/9/22 15:00
 * @description 配置类，时间类型与String类型转换
 * @reference
 * https://blog.csdn.net/weixin_30399155/article/details/101420370
 * https://www.cnblogs.com/knightdreams6/p/11733676.html
 * https://www.cnblogs.com/carrychan/p/9883172.html
 */
@Configuration
public class LocalDateTimeConverter {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    @Bean
    public Converter<String, LocalDateTime> LocalDateTimeConvert(){
        return new Converter<String, LocalDateTime>(){
            @Override
            public LocalDateTime convert(String value) {
                System.out.println("USE LOCALDATETIME CONVERTER");
                if (StringUtils.isEmpty(value)){
                    return null;
                }
                //先尝试ISO格式：2020-07-15T00：00：00
                try{
                    return LocalDateTime.parse(value);
                }catch (Exception e){
                    return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }
            }
        };
        /*lambda表达式
        return value -> {
            if (StringUtils.isEmpty(value)){
                return null;
            }
            return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        };
       */
    }

    @Bean
    public Converter<String, LocalDate> localDateConvert() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                System.out.println("USE LOCALDATE CONVERTER");
                if(source.trim().length() == 0){
                    return null;
                }
                try{
                    return LocalDate.parse(source);
                }catch (Exception e){
                    return LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }
            }
        };
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer(){
        JavaTimeModule module = new JavaTimeModule();
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        return builder -> {
            builder.simpleDateFormat(DATE_TIME_PATTERN);
            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_PATTERN)));
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));
            builder.modules(module);
        };
    }

}