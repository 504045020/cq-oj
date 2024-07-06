package com.cq.oj.common;

import lombok.Data;

import java.util.HashMap;

@Data
public class ResultUtil extends HashMap<String,Object> {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    public static final String CODE_TAG = "code";

    /**
     * 返回内容
     */
    public static final String MSG_TAG = "msg";

    /**
     * 数据对象
     */
    public static final String DATA_TAG = "data";

    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public ResultUtil() {
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     */
    public ResultUtil(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     */
    public <T> ResultUtil(int code, String msg, T data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (null != data) {
            super.put(DATA_TAG, data);
        }
    }


    /***
     * 操作成功
     */
    public static ResultUtil success() {
        return ResultUtil.success(ErrorCode.SUCCESS.getMessage());
    }


    /***
     * 操作成功
     */
    public static <T>ResultUtil success(T data) {
        return ResultUtil.success(ErrorCode.SUCCESS.getMessage(),data);
    }




    /**
     * 返回成功消息
     */
    public static <T>ResultUtil success(String msg, T data) {
        return new ResultUtil(ErrorCode.SUCCESS.NOT_LOGIN_ERROR.getCode(), msg, data);
    }



    /***
     * 操作成功
     */
    public static ResultUtil error() {
        return ResultUtil.success(ErrorCode.OPERATION_ERROR.getMessage(),null);
    }

    /**
     * 返回错误消息
     */
    public static ResultUtil error(String msg, Object data) {
        return new ResultUtil(ErrorCode.OPERATION_ERROR.getCode(), msg, data);
    }


    /**
     * 返回错误消息
     */
    public static ResultUtil error(int code, String message) {
        return new ResultUtil(code, message);
    }



    /**
     * 返回错误消息
     */
    public static ResultUtil error(ErrorCode errorCode, String message) {
        return new ResultUtil(errorCode.getCode(), message);
    }


}
