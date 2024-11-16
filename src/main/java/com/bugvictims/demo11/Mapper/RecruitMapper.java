package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.Recruit;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RecruitMapper {
    @Insert("INSERT INTO recruit (team_id, releaser_id, need_num, header, message, create_time, update_time)" +
            " VALUES (#{teamID}, #{releaserID}, #{needNum}, #{header}, #{message}, #{createTime}, #{updateTime})")
    void insertRecruit(Recruit recruit); // 插入数据，具体怎么插入，看数据库相应表的数据，notNull的规定，默认值的实现

    @Update("UPDATE recruit SET need_num = #{needNum}, " +
            "header = #{header}, " +
            "message = #{message}, " +
            "update_time = #{updateTime} " +
            "WHERE id = #{id}")
    void updateRecruit(Recruit recruit);

    List<Recruit> getRecruits(@Param("labelCondition") List<String> labelConditions);

    @Delete("DELETE FROM recruit WHERE id = #{id}")
    void deleteRecruit(int id);
}
