package com.cq.oj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cq.oj.common.ErrorCode;
import com.cq.oj.common.ServiceException;
import com.cq.oj.mapper.UserMapper;
import com.cq.oj.model.dto.user.UserAddRequest;
import com.cq.oj.model.dto.user.UserLoginRequest;
import com.cq.oj.model.entity.User;
import com.cq.oj.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
* @author 50404
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-07-05 16:14:14
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private  UserMapper userMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void login(UserLoginRequest userLoginRequest) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new ServiceException(ErrorCode.PARAM_ERROR,"账号或密码不能为空");
        }
        if(userAccount.length() < 4 || userAccount.length() > 10){
            throw new ServiceException(ErrorCode.PARAM_ERROR,"账号长度不符合要求");
        }
        if(userPassword.length() < 4 || userPassword.length() > 10){
            throw new ServiceException(ErrorCode.PARAM_ERROR,"密码长度不符合要求");
        }
        // 获取用户信息
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUserAccount, userAccount));
        // 将用户存信息存入Redis
        redisTemplate.opsForValue().set(userAccount, user,7L, TimeUnit.HOURS);

    }

}




