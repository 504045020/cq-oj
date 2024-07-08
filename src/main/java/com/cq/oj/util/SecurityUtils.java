package com.cq.oj.util;

import com.cq.oj.Aop.SecurityContextHolder;
import com.cq.oj.constant.CacheConstants;
import com.cq.oj.model.vo.LoginUserVo;

public class SecurityUtils {

    /**
     * 获取登录用户所有信息
     */
    public static LoginUserVo getUser() {
        return SecurityContextHolder.get(CacheConstants.LOGIN_TOKEN_KEY, LoginUserVo.class);
    }



}
