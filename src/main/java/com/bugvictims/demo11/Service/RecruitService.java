package com.bugvictims.demo11.Service;

import com.bugvictims.demo11.Pojo.Recruit;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface RecruitService {
    void releaseRecruit(Recruit recruit);

    void updateRecruit(Recruit recruit);

    PageInfo<Recruit> getRecruits(List<String> labels, int pageNum, int pageSize);

    void deleteRecruit(int id);
}
