package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.TeamUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeamUserMapper {
    @Insert("insert into team_user(id, team_id, user_id, type, create_time, update_time) "+
            "values(#{id}, #{teamId}, #{userId}, #{type}, now(), now())")
    void addTeamUser(TeamUser teamUser);
}
