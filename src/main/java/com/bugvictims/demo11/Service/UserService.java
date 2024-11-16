package com.bugvictims.demo11.Service;

import com.bugvictims.demo11.Pojo.User;

public interface UserService {
    //寻找用户名是否已被占用
    User findByUserName(String username);
    //注册新用户
    void register(String username, String password);

}
