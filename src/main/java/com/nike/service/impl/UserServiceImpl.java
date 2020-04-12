package com.nike.service.impl;

import com.nike.entity.User;
import com.nike.service.UserService;

public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
    @Override
    public User validateEmailExsit(String userEmail) {
        return null;
    }

    @Override
    public User validateUserExist(String userEmail) {
        return null;
    }

    @Override
    public User encryptedPassword(User user) {
        return null;
    }
}
