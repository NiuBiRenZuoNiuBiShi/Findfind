package com.bugvictims.demo11.Service;

import com.bugvictims.demo11.Mapper.RecruitMapper;
import com.bugvictims.demo11.Pojo.Recruit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecruitService {

    @Autowired
    private RecruitMapper recruitMapper;

    public void releaseRecruit(Recruit recruit) {
        recruitMapper.insertRecruit(recruit);
    }

}
