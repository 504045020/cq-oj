package com.cq.oj.common;


/***
 * 自定义异常类
 * @create 2021-08-09 16:04
 */
public class ServiceException extends RuntimeException{

    private final int code;


    public ServiceException(int code ,String message) {
        super(message);
        this.code = code;

    }

    public ServiceException(ErrorCode errorCode,String message){
        super(message);
        this.code = errorCode.getCode();
    }


    public int getCode() {
        return code;
    }

}
