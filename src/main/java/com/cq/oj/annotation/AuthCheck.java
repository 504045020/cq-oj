package com.cq.oj.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface  AuthCheck {

    /***
     * 必须有该权限才可操作
     */
    String mustRole() default "";
}
