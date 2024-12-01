package com.bugvictims.demo11.Service;

import com.bugvictims.demo11.Pojo.JoinRequest;
import com.bugvictims.demo11.Pojo.Recruit;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface RecruitService {
    Integer insertRecruit(Recruit recruit);

    void updateRecruit(Recruit recruit);

    PageInfo<Recruit> selectRecruits(List<String> labels, Integer pageNum, Integer pageSize);

    boolean deleteRecruit(Integer id);

    Integer insertJoinRequest(Integer recruitID, JoinRequest joinRequest);

    void insertRecruitFiles(Recruit recruit);

    void insertJoinFiles(JoinRequest joinRequest);
}
