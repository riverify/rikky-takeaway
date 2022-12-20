package com.fubukiss.rikky.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Project: rikky-takeaway - MybatisPlusConfig 配置MP分页插件
 * <p>Powered by Riverify On 12-20-2022 16:25:03
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor()); // PaginationInnerInterceptor MP提供的分页插件
        return interceptor;
    }
}
