package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.JoinRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface JoinRequestMapper {
    @Insert("INSERT INTO join_request (user_id, team_id, message, response, create_time, update_time)" + "VALUES (#{userId}, #{teamId}, #{message}, #{response}, #{createTime}, #{updateTime})")
    void insertJoinRequest(JoinRequest joinRequest);

    //查找加入请求
    @Select("SELECT * FROM join_request WHERE id = #{requestId}")
    JoinRequest getJoinRequestById(int requestId);
}
