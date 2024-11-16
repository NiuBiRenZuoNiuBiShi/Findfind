package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.Seeker;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SeekerLabelMapper {

    void insertSeekerLabel(Seeker seeker);

    @Delete("DELETE FROM seeker_label WHERE seeker_id = #{seekerID}")
    void deleteSeekerLabelBySeekerID(int seekerID);
}
