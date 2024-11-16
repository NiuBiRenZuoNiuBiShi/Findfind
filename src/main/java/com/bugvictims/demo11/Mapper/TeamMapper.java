package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.Team;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TeamMapper {

    //创建队伍
    @Insert("insert into teams( name, description, type, status, position, create_time, update_time) " + "values(#{name}, #{description}, #{type}, #{status}, #{position}, now(), now())")
    void createTeam(Team team);

    //更新队伍
    @Update("update teams set name=#{name}, description=#{description}, type=#{type}, status=#{status}, position=#{position}, update_time=now() where id=#{id}")
    void updateTeam(Team team);

    //获取队伍信息By id 排除已解散队伍
    @Select("select * from teams where id = #{id} and status != 3")
    Team getTeamById(int id);

    //获取队伍人数
    @Select("select count(*) from team_user where team_id = #{teamId}")
    int getTeamUserCount(int teamId);

    //删除队伍
    @Update("update teams set status=3 where id=#{teamId}")
    void deleteTeam(int teamId);
    
}
