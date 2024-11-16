package com.bugvictims.demo11.Service.impl;

import com.bugvictims.demo11.Mapper.RecruitMapper;
import com.bugvictims.demo11.Pojo.Recruit;
import com.bugvictims.demo11.Service.RecruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecruitServiceImpl implements RecruitService {

    @Autowired
    private RecruitMapper recruitMapper;

    public void releaseRecruit(Recruit recruit) {
        recruitMapper.insertRecruit(recruit); // 调用Mapper
    }

}
