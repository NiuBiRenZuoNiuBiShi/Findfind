package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    //用户名查找
    @Select("select * from user where username=#{username}")
    User findByUserName(String username);

    //添加用户
    @Insert("INSERT INTO user(username, password, phone, biology, status, create_time, update_time) " + "VALUES (#{username}, #{password}, #{phone}, #{biology}, #{status}, NOW(), NOW())")
    void add(User user);

    //通过id获取用户
    @Select("select * from user where id=#{id}")
    User getUserById(int id);

    //更新用户信息
    @Update("update user set nick_name=#{nickName},email=#{email},phone=#{phone}," +
            "biology=#{biology},status=#{status}," +
            "update_time=#{updateTime},position=#{position}  where id=#{id}")
    void update(User user);
}
