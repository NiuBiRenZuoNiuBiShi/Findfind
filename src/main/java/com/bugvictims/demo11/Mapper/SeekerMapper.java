package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.Seeker;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SeekerMapper {

    @Insert("INSERT INTO seeker (seeker_id, header, message, position, create_time, update_time)" +
        " VALUES (#{seekerID}, #{header}, #{message}, #{position}, #{createTime}, #{updateTime})" )
    void createSeeker(Seeker seeker);

    @Delete("DELETE FROM seeker WHERE seeker_id = #{id}")
    void deleteSeeker(int id);

    @Update("UPDATE seeker SET header = #{header}, message = #{message}" +
            ", position = #{position}, update_time = #{updateTime}")
    void updateSeeker(Seeker seeker);

    List<Seeker> selectSeekers(List<String> labels);
}
