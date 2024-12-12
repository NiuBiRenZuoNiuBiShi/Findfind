package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.Seeker;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SeekerMapper {

    @Insert("INSERT INTO seeker (seeker_id, header, message, position, create_time, update_time)" +
        " VALUES (#{seekerID}, #{header}, #{message}, #{position}, #{createTime}, #{updateTime})" )
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer createSeeker(Seeker seeker);

    @Delete("DELETE FROM seeker WHERE seeker_id = #{id}")
    void deleteSeeker(Integer id);

    @Update("UPDATE seeker SET header = #{header}, message = #{message}" +
            ", position = #{position}, update_time = #{updateTime} WHERE id = #{id}")
    void updateSeeker(Seeker seeker);

    List<Seeker> selectSeekers(List<String> labels);

    @Select("SELECT id, seeker_id, header, message, position, create_time, update_time FROM seeker WHERE id = #{id}")
    Seeker selectSeekerById(Integer id);

    @Select("SELECT seeker_id FROM seeker WHERE id = #{id}")
    Integer selectUserIDBySeekerID(Integer id);

    @Select("SELECT * FROM seeker WHERE seeker_id = #{id}")
    List<Seeker> selectSeekersByUserID(Integer id);
}
