package com.cq.oj.Aop;

import com.cq.oj.common.ErrorCode;
import com.cq.oj.common.ServiceException;
import com.cq.oj.constant.CacheConstants;
import com.cq.oj.model.vo.LoginUser;
import com.cq.oj.util.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

public class LoginCheckHandlerInterceptor implements HandlerInterceptor {

    @Resource
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 不是映射方法就放行
        if (!(handler instanceof HandlerMethod)){
           return true;
        }
        String token = request.getHeader(CacheConstants.AUTHENTICATION);

        if(StringUtils.isAnyBlank(token)){

        }
        // 查看缓存是否存在
        LoginUser loginUser = redisService.getCacheObject(CacheConstants.LOGIN_TOKEN_KEY + token);
        if(null != loginUser && null != loginUser.getUser()){
            // 刷新过期时间
            redisService.expire(CacheConstants.LOGIN_TOKEN_KEY + token, CacheConstants.EXPIRATION, TimeUnit.SECONDS);
            SecurityContextHolder.set(CacheConstants.LOGIN_TOKEN_KEY,loginUser);
        } else {
            response.setStatus(401);
            response.getWriter().write("The login status has expired or the token is incorrect.");
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter()
    {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置访问源地址
        config.addAllowedOriginPattern("*");
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 有效期 1800秒
        config.setMaxAge(1800L);
        // 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 返回新的CorsFilter
        return new CorsFilter(source);
    }

}
