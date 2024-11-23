package com.bugvictims.demo11.Service;

import com.bugvictims.demo11.Pojo.JoinRequest;
import com.bugvictims.demo11.Pojo.User;
import com.bugvictims.demo11.Pojo.UserIgnorePassword;
import com.github.pagehelper.PageInfo;

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

    void userJoin(JoinRequest joinRequest);

    void logout(User loginUser);
    //用户列表
    PageInfo<User>getUser(Integer page, Integer size);
}
