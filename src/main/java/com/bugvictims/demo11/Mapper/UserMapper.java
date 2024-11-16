package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    //用户名查找
    @Select("select * from user where username=#{username}")
    User findByUserName(String username);
    //添加用户
    @Insert("insert into user(username,password,creat_time,update_time)*+" +
            " value(#{username},#{password},now(),now())")
    void add(String username, String password);
}
