package com.bugvictims.demo11.Service.impl;
import com.bugvictims.demo11.Mapper.UserMapper;
import com.bugvictims.demo11.Pojo.User;
import  com.bugvictims.demo11.Service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    private UserMapper userMapper;
    //寻找用户名
    @Override
    public User findByUserName(String username) {
        User u=userMapper.findByUserName(username);
        return u;
    }
    //注册新用户
    @Override
    public void register(String username, String password) {
        //加密处理，此处暂无，后期再加
        //添加
        userMapper.add(username,password);
    }
}
