package com.bugvictims.demo11.Controller;
import  com.bugvictims.demo11.Pojo.Result;
import  com.bugvictims.demo11.Pojo.User;
import  com.bugvictims.demo11.Service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userServiceImpl;
    @PostMapping("/register")
    public Result register(String username,String password){
       User u=userServiceImpl.findByUserName(username);
       if(u==null){
           //没注册过
           userServiceImpl.register(username,password);
           return new Result().success();
       }else{
           //占用
           return new Result().error("用户名已被占用");
       }
    }


}
