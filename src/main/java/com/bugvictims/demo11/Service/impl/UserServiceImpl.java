package com.bugvictims.demo11.Service.impl;

import com.bugvictims.demo11.Mapper.UserMapper;
import com.bugvictims.demo11.Pojo.User;
import com.bugvictims.demo11.Pojo.UserIgnorePassword;
import com.bugvictims.demo11.Service.UserService;
import com.bugvictims.demo11.Utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    //寻找用户名
    @Override
    public User findByUserName(String username) {
        User u = userMapper.findByUserName(username);
        return u;
    }
    //注册新用户
    @Override
    public void register(UserIgnorePassword user) {
        //加密处理，此处暂无，后期再加
        //添加
        userMapper.add(user);
    }
    //获取当前登录用户
    @Override
    public User getLoginUser() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        //根据用户名查询用户
        return findByUserName(username);
    }
    @Override
    public User getUserById(int id) {
        return userMapper.getUserById(id);
    }

    //更新用户信息
    @Override public void update(User user){

        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }
}
