package com.cq.oj.config;

import com.cq.oj.Aop.LoginCheckHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class handlerConfig implements WebMvcConfigurer {
    /**
     * 不需要拦截地址
     */
    public static final String[] EXCLUDE_URLS = {"/user/login",
            "/swagger-resources/**",
            "/v2/**"};

    public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
        registry.addInterceptor(initHeaderInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_URLS)
                .order(-10);
    }


    @Bean
    public LoginCheckHandlerInterceptor initHeaderInterceptor() {
        return new LoginCheckHandlerInterceptor();
    }

}
