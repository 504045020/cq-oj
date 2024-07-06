package com.cq.oj.common;

import lombok.Getter;

@Getter
public enum ErrorCode {

    SUCCESS(0, "操作成功"),

    PARAM_ERROR(7001, "参数错误"),

    NOT_LOGIN_ERROR(7002, "未登录"),

    NOT_AUTH_ERROR(7003, "无权限"),

    NOT_FOUND_ERROR(7004, "请求数据不存在"),

    FORBIDDEN_ERROR(7005, "禁止访问"),

    SYSTEM_ERROR(7006, "系统内部异常"),

    OPERATION_ERROR(7007, "操作失败"),

    API_REQUEST_ERROR(7008, "接口不存在");

    /***
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;


    ErrorCode(int code, String message){
        this.code = code;
        this.message = message;
    }

}
