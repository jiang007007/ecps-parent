package com.nike.service;

import com.nike.entity.User;

/**
 * 用户业务接口
 */
public interface UserService extends BaseService<User> {
    //验证邮箱是否存在
    User validateEmailExsit(String userEmail);

    //验证用户是否存在，被激活了的邮箱才算是存在的
    User validateUserExist(String userEmail);

    //对用户的密码进行加密
    User encryptedPassword(User user);
}
