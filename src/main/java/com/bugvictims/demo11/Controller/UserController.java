package com.bugvictims.demo11.Controller;

import com.bugvictims.demo11.Pojo.*;
import com.bugvictims.demo11.Service.impl.UserServiceImpl;
import com.bugvictims.demo11.Utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userServiceImpl;
    @PostMapping("/register")
    public Result register(@RequestBody @Validated  User user) {
        // 检查必填字段是否已提供
        if (user.getUsername() == null || user.getPassword() == null || user.getPhone() == null || user.getBiology() == null || (user.getStatus()!=1&&user.getStatus()!=0)){
            return new Result().error("All required fields must be filled out.");
        }
        User u = userServiceImpl.findByUserName(user.getUsername());
        if (u == null) {
            //没注册过
            userServiceImpl.register(user);
            return new Result().success();
        } else {
            //占用
            return new Result().error("用户名已被占用");
        }
    }
    @PostMapping("/login")
    public Result login(String username, String password) {
        User loginUser = userServiceImpl.findByUserName(username);
        if (loginUser == null) {
            return new Result().error("用户名不存在");
        }
        if (loginUser.getPassword().equals(password)) {
            //生成jwt-token令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());
            String token = JWTUtils.generateToken(claims);
            return new Result().success(token);
        } else {
            return new Result().error("密码错误");
        }
    }
    //查看用户信息
    @PostMapping("/userInfo")
    public Result userInfo(){
        User user=userServiceImpl.getLoginUser();
        if(user!=null) {
            return new Result().success(user);
        }
        else
            return new Result().error("无用户登录");
    }
    //更新用户信息
    @PostMapping("/update")
    public Result update(@RequestBody @Validated User user){
        User u=userServiceImpl.getLoginUser();
        if(u!=null){
            userServiceImpl.update(user);
            return new Result().success();
        }
        else
            return new Result().error("无用户登录");
    }






}
