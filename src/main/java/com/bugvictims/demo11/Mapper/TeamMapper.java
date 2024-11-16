package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.Team;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TeamMapper {

    //创建队伍
    @Insert("insert into teams( name, description, type, status, position, create_time, update_time) "+
            "values(#{name}, #{description}, #{type}, #{status}, #{position}, now(), now())")
    void createTeam(Team team);

    //更新队伍
    @Update("update teams set name=#{name}, description=#{description}, type=#{type}, status=#{status}, position=#{position}, update_time=now() where id=#{id}")
    void updateTeam(Team team);
}
