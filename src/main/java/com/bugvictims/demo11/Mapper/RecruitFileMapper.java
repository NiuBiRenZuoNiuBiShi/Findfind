package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.PojoFile;
import com.bugvictims.demo11.Pojo.Recruit;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RecruitFileMapper {

    void insertRecruitFiles(Recruit recruit);

    List<PojoFile> selectRecruitFiles(Recruit recruit);

    List<PojoFile> selectRecruitFilesByRecruitIds(List<Integer> recruitIds);

    @Delete("DELETE FROM recruit_file WHERE id = #{id}")
    void deleteRecruitFile(Integer id);

    @Select("SELECT * FROM recruit_file WHERE recruit_id=#{id}")
    @Results({
            @Result(property = "fileData", column = "file"),
            @Result(property = "linkedID", column = "seeker_id")
    })
    List<PojoFile> selectRecruitFilesByRecruitId(Integer id);
}
