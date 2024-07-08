package com.cq.oj.Aop;

import com.cq.oj.common.ErrorCode;
import com.cq.oj.common.ServiceException;
import com.cq.oj.constant.CacheConstants;
import com.cq.oj.model.vo.LoginUserVo;
import com.cq.oj.util.RedisService;
import org.apache.commons.lang3.StringUtils;
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
            throw  new ServiceException(ErrorCode.OPERATION_ERROR,"token is null");
        }
        // 查看缓存是否存在
        LoginUserVo loginUserVo = redisService.getCacheObject(CacheConstants.LOGIN_TOKEN_KEY +replaceTokenPrefix(token));
        if(null != loginUserVo){
            // 刷新过期时间
            redisService.expire(CacheConstants.LOGIN_TOKEN_KEY + token, CacheConstants.EXPIRATION, TimeUnit.MINUTES);
            SecurityContextHolder.set(CacheConstants.LOGIN_TOKEN_KEY, loginUserVo);
        } else {
            response.setStatus(401);
            response.getWriter().write("The login status has expired or the token is incorrect.");
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SecurityContextHolder.remove();
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

    /**
     * 裁剪token前缀
     */
    public static String replaceTokenPrefix(String token) {
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(CacheConstants.BEARER)) {
            token = token.replaceFirst(CacheConstants.BEARER, "").trim();
        }
        return token;
    }

}
