package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.Recruit;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecruitMapper {
    @Insert("INSERT INTO recruit (team_id, releaser_id, need_num, header, message, create_time, update_time)" +
            " VALUES (#{teamID}, #{releaserID}, #{needNum}, #{header}, #{message}, #{createTime}, #{updateTime})")
    void insertRecruit(Recruit recruit); // 插入数据， 具体怎么插入，看数据库相应表的数据，notNull的规定，默认值的实现
}
