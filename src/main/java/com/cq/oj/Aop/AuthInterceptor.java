package com.cq.oj.Aop;

import com.cq.oj.annotation.AuthCheck;
import com.cq.oj.common.ErrorCode;
import com.cq.oj.common.ServiceException;
import com.cq.oj.model.enums.UserRoleEnum;
import com.cq.oj.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Aspect
@Component
public class AuthInterceptor {


    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck)throws Throwable{
        // 获取注解鉴权信息
        String mustRole = authCheck.mustRole();
        // 获取登陆用户权限

        if(null != mustRole){
            String role = Objects.requireNonNull(UserRoleEnum.getEnumByValue(mustRole)).getValue();
            if(null == role){
                throw new ServiceException(ErrorCode.NOT_AUTH_ERROR);
            }
            // 判断是否为管理员
            if(role.equals(mustRole)){

            }
        }

        return joinPoint.proceed();
    }

}
