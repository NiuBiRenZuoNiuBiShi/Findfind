package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.TeamUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TeamUserMapper {
    //添加团队成员
    @Insert("insert into team_user(id, team_id, user_id, type, create_time, update_time) " + "values(#{id}, #{teamId}, #{userId}, #{type}, now(), now())")
    void addTeamUser(TeamUser teamUser);

    //查找指定id团队成员
    @Select("select * from team_user where team_id = #{teamId} and user_id = #{userId}")
    TeamUser findTeamUserByTeamIdAndUserId(int teamId, int userId);

    //查找所有团队成员
    @Select("select * from team_user where team_id = #{teamId}")
    List<TeamUser> findTeamUsersByTeamId(int teamId);

    //更新团队成员
    @Update("update team_user set type=#{type}, update_time=now() where team_id=#{teamId} and user_id=#{userId}")
    void updateTeamUser(TeamUser teamUser);

    //删除团队成员
    @Update("update team_user set type=#{type} where team_id=#{teamId} and user_id=#{userId}")
    void deleteTeamUser(int teamId, int userId, String type);
}
