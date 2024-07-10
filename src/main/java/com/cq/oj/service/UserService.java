package com.cq.oj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cq.oj.model.dto.user.UserAddRequest;
import com.cq.oj.model.dto.user.UserLoginRequest;
import com.cq.oj.model.dto.user.UserQueryRequest;
import com.cq.oj.model.dto.user.UserRegisterRequest;
import com.cq.oj.model.entity.User;
import com.cq.oj.model.vo.LoginUserVo;
import com.cq.oj.model.vo.UserVO;

import java.util.List;

/**
* @author 50404
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-07-05 16:14:14
*/
public interface UserService extends IService<User> {

    public int addUser(UserAddRequest userAddRequest);

    public List<UserVO> listUser(List<User> userList);

    LoginUserVo login(UserLoginRequest userLoginRequest);

    long Register(UserRegisterRequest userRegisterRequest);


    UserVO getUserVO(User user);
}
