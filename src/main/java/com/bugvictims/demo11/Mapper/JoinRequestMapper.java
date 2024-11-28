package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.JoinRequest;
import org.apache.ibatis.annotations.*;

@Mapper
public interface JoinRequestMapper {
    @Insert("INSERT INTO join_request (user_id, team_id, message, response, create_time, update_time)" + "VALUES (#{userId}, #{teamId}, #{message}, #{response}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertJoinRequest(JoinRequest joinRequest);

    //查找加入请求
    @Select("SELECT * FROM join_request WHERE id = #{requestId}")
    JoinRequest getJoinRequestById(int requestId);

    //查找处理请求By teamId and userId
    @Select("SELECT * FROM join_request WHERE team_id = #{teamId} AND user_id = #{userId}")
    JoinRequest getJoinRequestByTeamIdAndUserId(int teamId, int userId);

    //更新请求状态
    @Update("UPDATE join_request SET status = #{status}, response = #{response} WHERE id = #{requestId}")
    void updateJoinRequestStatus(int requestId, int statue, String response);

    //删除请求
    @Delete("DELETE FROM join_request WHERE id = #{requestId}")
    void deleteJoinRequest(int requestId);
}
