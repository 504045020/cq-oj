package com.cq.oj.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResultUtil handleException(ServiceException e){
        log.error("ServiceException:",e);
        return ResultUtil.error(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResultUtil runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtil.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }


}
