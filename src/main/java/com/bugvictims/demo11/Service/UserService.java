package com.bugvictims.demo11.Service;

import com.bugvictims.demo11.Pojo.User;
import com.bugvictims.demo11.Pojo.UserIgnorePassword;

public interface UserService {
    //寻找用户名是否已被占用
    User findByUserName(String username);

    //注册新用户
    void register(UserIgnorePassword user);

    //获取登录用户
    User getLoginUser();

    //通过id获取用户
    User getUserById(int id);

    void update(User user);
}
