package com.bugvictims.demo11.Service.impl;

import com.bugvictims.demo11.Mapper.RecruitMapper;
import com.bugvictims.demo11.Pojo.Recruit;
import com.bugvictims.demo11.Service.RecruitService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bugvictims.demo11.Mapper.RecruitLabelMapper;

import java.util.List;

@Service
public class RecruitServiceImpl implements RecruitService {

    @Autowired
    private RecruitMapper recruitMapper;

    @Autowired
    private RecruitLabelMapper recruitLabelMapper;

    public void releaseRecruit(Recruit recruit) {
        recruitMapper.insertRecruit(recruit); // 调用Mapper
        recruitLabelMapper.addRecruitLabel(recruit);
    }

    public void updateRecruit(Recruit recruit) {
        recruitMapper.updateRecruit(recruit);
        recruitLabelMapper.addRecruitLabel(recruit);
    }

    public PageInfo<Recruit> getRecruits(List<String> labels, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize); //分页查询...
        List<Recruit> recruits = recruitMapper.getRecruits(labels);
        return new PageInfo<>(recruits);
    }

    public void deleteRecruit(int id) {
        recruitMapper.deleteRecruit(id);
        recruitLabelMapper.deleteRecruitLabel(id);
    }
}
