package com.cq.oj.model;

import com.cq.oj.model.entity.User;

/***
 * 用户信息
 */
public class UserHolder {

    private static final ThreadLocal<User> userHolder = new ThreadLocal<>();

    public static void saveUser(User user) {
        userHolder.set(user);
    }

    public static User getUser() {
        return userHolder.get();
    }

    public static void removeUser() {
        userHolder.remove();
    }
}
