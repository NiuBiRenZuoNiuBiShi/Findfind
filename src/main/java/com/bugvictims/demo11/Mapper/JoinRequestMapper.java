package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.JoinRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JoinRequestMapper {
    @Insert("INSERT INTO join_request (user_id, team_id, message, response, create_time, update_time)" +
            "VALUES (#{userId}, #{teamId}, #{message}, #{response}, #{createTime}, #{updateTime})")
    void insertJoinRequest(JoinRequest joinRequest);
}
