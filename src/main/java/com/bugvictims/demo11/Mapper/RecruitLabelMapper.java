package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.Label;
import com.bugvictims.demo11.Pojo.Recruit;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RecruitLabelMapper {

    void addRecruitLabel(Recruit recruit); // 增加label，因为传递的Recruit的Label会有多个，采用XML的ForEach

    @Delete("DELETE FROM recruit_label WHERE recruit_id = #{id}")
    void deleteRecruitLabel(int id);

    List<Label> selectRecruitLabelByRecruitIds(List<Integer> recruitIds);
}