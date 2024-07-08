package com.cq.oj.model.dto.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(description = "添加用户")
public class UserAddRequest implements Serializable {

    /**
     * 用户账号
     */
    @ApiModelProperty(value = "账号")
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户角色: user, admin
     */
    private String userRole;


    private static final long serialVersionUID = 1L;
}
