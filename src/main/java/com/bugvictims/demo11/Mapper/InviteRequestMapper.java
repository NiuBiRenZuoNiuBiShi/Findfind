package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.InviteRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InviteRequestMapper {

    @Insert("INSERT INTO invite_request (user_id, team_id, releaser_id, message, response, header, create_time, update_time)" +
            " VALUES (#{userId}, #{teamId}, #{releaseID}, #{message}, #{response}, #{header}, #{createTime}, #{updateTime})")
    void insertInviteRequest(InviteRequest inviteRequest);
}