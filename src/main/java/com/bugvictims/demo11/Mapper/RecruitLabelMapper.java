package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.Recruit;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecruitLabelMapper {

    void addRecruitLabel(Recruit recruit); // 增加label，因为传递的Recruit的Label会有多个，采用XML的ForEach

    void updateRecruitLabel(Recruit recruit); // 同上

    @Delete("DELETE FROM recruit_label WHERE recruit_id = #{id}")
    void deleteRecruitLabel(int id);
}