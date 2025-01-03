package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.InviteRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface InviteRequestMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO invite_request (user_id, team_id, releaser_id, message, response, create_time, update_time)" + " VALUES (#{userID}, #{teamID}, #{releaserID}, #{message}, #{response}, #{createTime}, #{updateTime})")
    Integer insertInviteRequest(InviteRequest inviteRequest);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO invite_request (user_id, team_id,  message, create_time, update_time)" + " VALUES (#{userID}, #{teamID}, #{message}, #{createTime}, #{updateTime})")
    void createInviteRequest(int teamId, int userId, String message);

    @Select("SELECT * FROM invite_request WHERE team_id = #{teamID}")
    List<InviteRequest> getInviteRequestsByTeamId(Integer teamID);

    @Select("SELECT * FROM invite_request WHERE user_id = #{userID}")
    List<InviteRequest> getInviteRequestsByUserId(Integer userID);

    @Select("SELECT * FROM invite_request WHERE id = #{ID}")
    InviteRequest getInviteRequestById(Integer ID);

    //查找处理请求By teamId and userId
    @Select("SELECT * FROM invite_request WHERE team_id = #{teamId} AND user_id = #{userId} AND status = 0")
    InviteRequest getInviteRequestByTeamIdAndUserId(int teamId, int userId);

    //更新请求状态
    @Update("UPDATE invite_request SET status = #{status}, response = #{response} WHERE id = #{requestId}")
    void updateInviteRequestStatus(int requestId, int status, String response);

    //删除请求
    @Delete("DELETE FROM invite_request WHERE id = #{requestId}")
    void deleteInviteRequest(int requestId);

}
