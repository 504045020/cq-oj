package com.cq.oj.controller;

import com.cq.oj.common.ResultUtil;
import com.cq.oj.model.dto.user.UserAddRequest;
import com.cq.oj.model.dto.user.UserLoginRequest;
import com.cq.oj.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    public UserService userService;

    @PostMapping
    public ResultUtil login(@RequestBody UserLoginRequest userLoginRequest) {
        userService.login(userLoginRequest);
        return ResultUtil.success();
    }


    @PutMapping
    public ResultUtil addUser(@RequestBody UserAddRequest userAddRequest) {
        return ResultUtil.success();
    }


    @GetMapping
    public ResultUtil list(){
        return  ResultUtil.success(userService.list());
    }


}
