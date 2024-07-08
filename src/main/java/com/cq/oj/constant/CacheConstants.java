package com.cq.oj.constant;

public interface CacheConstants {

    public final static Long EXPIRATION = 7L;

    /**
     * 权限缓存前缀
     */
    public final static String LOGIN_TOKEN_KEY = "login_token:";

    /**
     * access_token缓存前缀
     */
    public final static String ACCESS_TOKEN_KEY = "oj_token:";

    /**
     * 令牌自定义标识
     */
    public static final String AUTHENTICATION = "Authorization";

    /**
     * 令牌自定义标识
     */
    public static final String BEARER = "Bearer";
}
