package com.bugvictims.demo11.Controller;
import  com.bugvictims.demo11.Pojo.*;
import  com.bugvictims.demo11.Service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userServiceImpl;
    @PostMapping("/register")
    public Result register(@RequestBody User  user){
        // 检查必填字段是否已提供
        if (user.getUsername() == null || user.getPassword() == null ||
                user.getPhone() == null || user.getBiology() == null ||
                user.getStatus() == 2) {
            return new Result().error("All required fields must be filled out.");
        }
       User u=userServiceImpl.findByUserName(user.getUsername());
       if(u==null){
           //没注册过
           userServiceImpl.register(user);
           return new Result().success();
       }else{
           //占用
           return new Result().error("用户名已被占用");
       }
    }
}
