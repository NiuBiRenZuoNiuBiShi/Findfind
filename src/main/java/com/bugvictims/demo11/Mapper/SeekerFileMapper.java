package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.PojoFile;
import com.bugvictims.demo11.Pojo.Seeker;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SeekerFileMapper {

    void insertSeekerFile(Seeker seeker);

    @Delete("DELETE FROM seeker_file WHERE seeker_id = #{id}")
    void deleteSeekerFile(Integer id);

    @Select("SELECT (id, seeker_id, file, type, create_time, update_time)" +
            "FROM seeker_file" +
            " WHERE seeker_id = #{id}")
    List<PojoFile> selectSeekerFile(Seeker seeker);

    List<PojoFile> selectSeekerFileByIds(List<Integer> seekerIds);

    @Select("SELECT * FROM seeker_file WHERE seeker_id = #{seekerId}")
    @Results({
            @Result(property = "fileData", column = "file"),
            @Result(property = "linkedID", column = "seeker_id")
    })
    List<PojoFile> selectSeekerFileBySeekerId(Integer seekerId);
}
