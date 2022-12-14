package com.fubukiss.rikky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * <p>Project: rikky-takeaway - WebMvcConfig 用于静态资源映射
 * <p>Powered by Riverify On 12-14-2022 23:52:20
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {


    /**
     * 设置静态资源映射
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射...");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }
}
