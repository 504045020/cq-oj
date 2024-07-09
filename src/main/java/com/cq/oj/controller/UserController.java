package com.cq.oj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cq.oj.annotation.AuthCheck;
import com.cq.oj.common.ErrorCode;
import com.cq.oj.common.ResultUtil;
import com.cq.oj.common.ServiceException;
import com.cq.oj.constant.AuthConstant;
import com.cq.oj.model.dto.user.*;
import com.cq.oj.model.entity.User;
import com.cq.oj.model.vo.LoginUserVo;
import com.cq.oj.service.UserService;
import com.cq.oj.util.MD5Util;
import com.cq.oj.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api(tags = "用户管理")
@RequestMapping("user")
public class UserController {

    @Resource
    public UserService userService;

    @ApiOperation("用户登陆")
    @PostMapping("/login")
    public ResultUtil login(@RequestBody UserLoginRequest userLoginRequest) {
        return ResultUtil.success(userService.login(userLoginRequest));
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public ResultUtil register(@RequestBody UserRegisterRequest userRegisterRequest) {
        if(null == userRegisterRequest){
            throw  new ServiceException(ErrorCode.PARAM_ERROR);
        }
        return ResultUtil.success(userService.Register(userRegisterRequest));
    }

    @ApiOperation("新增用户")
    @PostMapping("/addUser")
    @AuthCheck(mustRole = AuthConstant.ADMIN_ROLE)
    public ResultUtil addUser(@RequestBody UserAddRequest userAddRequest) {
        if(null == userAddRequest){
            throw  new ServiceException(ErrorCode.PARAM_ERROR);
        }
        return ResultUtil.success(userService.addUser(userAddRequest));
    }

    @ApiOperation("分页获取所有用户")
    @AuthCheck(mustRole = AuthConstant.ADMIN_ROLE)
    @PostMapping("/list")
    public ResultUtil list(@RequestBody UserQueryRequest userQueryRequest){
        long current = userQueryRequest.getCurrent();
        long pageSize = userQueryRequest.getPageSize();
        Page<User> page =
                userService.page(new Page<>(current, pageSize), userService.getQueryWrapper(userQueryRequest));
        return ResultUtil.success(page);
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    @AuthCheck(mustRole = AuthConstant.ADMIN_ROLE)
    public ResultUtil delete(@PathVariable("id") Long id){
        if(id == null || id < 0){
            throw new ServiceException(ErrorCode.PARAM_ERROR);
        }
        return ResultUtil.success(userService.removeById(id));
    }

    @ApiOperation("更新个人信息")
    @PostMapping("/update")
    public ResultUtil updateUser(@RequestBody UserUpdateRequest userUpdateRequest){
        if(null == userUpdateRequest){
            throw  new ServiceException(ErrorCode.PARAM_ERROR);
        }
        Long id = SecurityUtils.getUser().getId();
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest,user);
        user.setId(id);
        userService.updateById(user);
        return ResultUtil.success();
    }

}
