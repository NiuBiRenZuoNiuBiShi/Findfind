package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.InviteRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface InviteRequestMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO invite_request (user_id, team_id, releaser_id, message, response, header, create_time, update_time)" +
            " VALUES (#{userID}, #{teamID}, #{releaseID}, #{message}, #{response}, #{header}, #{createTime}, #{updateTime})")
    Integer insertInviteRequest(InviteRequest inviteRequest);

    @Select("SELECT * FROM invite_request WHERE team_id = #{teamID}")
    List<InviteRequest> getInviteRequestsByTeamId(Integer teamID);

    @Select("SELECT * FROM invite_request WHERE user_id = #{userID}")
    List<InviteRequest> getInviteRequestsByUserId(Integer userID);

    @Select("SELECT * FROM invite_request WHERE id = #{ID}")
    List<InviteRequest> getInviteRequestByReleaserId(Integer ID);
}
