package com.cq.oj.model.vo;

import com.cq.oj.model.entity.User;
import lombok.Data;

@Data
public class LoginUser {

    private String token;

    private User user;


}
