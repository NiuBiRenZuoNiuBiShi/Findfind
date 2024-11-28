package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.PojoFile;
import com.bugvictims.demo11.Pojo.Recruit;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RecruitFileMapper {

    void insertRecruitFiles(Recruit recruit);

    List<PojoFile> selectRecruitFiles(Recruit recruit);

    @Delete("DELETE FROM recruit_file WHERE id = #{id}")
    void deleteRecruitFile(Integer id);
}
