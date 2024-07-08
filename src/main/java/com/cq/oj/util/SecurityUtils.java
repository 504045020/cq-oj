package com.cq.oj.util;

import com.cq.oj.Aop.SecurityContextHolder;
import com.cq.oj.constant.CacheConstants;
import com.cq.oj.model.entity.User;
import com.cq.oj.model.vo.LoginUser;

public class SecurityUtils {

    /**
     * 获取登录用户所有信息
     */
    public static LoginUser getLoginUser() {
        return SecurityContextHolder.get(CacheConstants.LOGIN_TOKEN_KEY,LoginUser.class);
    }

    /**
     * 获取登录用户信息
     */
    public static User getPublicUser() {
        return getLoginUser().getUser();
    }


}
