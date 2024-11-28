package com.bugvictims.demo11.Service.impl;

import com.bugvictims.demo11.Mapper.JoinRequestMapper;
import com.bugvictims.demo11.Mapper.RecruitFileMapper;
import com.bugvictims.demo11.Mapper.RecruitMapper;
import com.bugvictims.demo11.Pojo.JoinRequest;
import com.bugvictims.demo11.Pojo.Recruit;
import com.bugvictims.demo11.Service.RecruitService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bugvictims.demo11.Mapper.RecruitLabelMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecruitServiceImpl implements RecruitService {

    @Autowired
    private RecruitMapper recruitMapper;

    @Autowired
    private RecruitLabelMapper recruitLabelMapper;

    @Autowired
    private JoinRequestMapper joinRequestMapper;

    @Autowired
    private RecruitFileMapper recruitFileMapper;

    public Integer releaseRecruit(Recruit recruit) {
        recruit.setCreateTime(LocalDateTime.now()); // 添加一些数据
        recruit.setUpdateTime(LocalDateTime.now());
        Integer recruitID =  recruitMapper.insertRecruit(recruit); // 调用Mapper
        recruitLabelMapper.addRecruitLabel(recruit);
        return recruitID;
    }

    public void updateRecruit(Recruit recruit) {
        recruitMapper.updateRecruit(recruit);
        recruitLabelMapper.deleteRecruitLabel(recruit.getId());
        recruitLabelMapper.addRecruitLabel(recruit);
        recruitFileMapper.deleteRecruitFile(recruit.getId());
        recruitFileMapper.insertRecruitFiles(recruit);
    }

    public PageInfo<Recruit> getRecruits(List<String> labels, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize); //分页查询...
        List<Recruit> recruits = recruitMapper.getRecruits(labels);
        for (Recruit recruit: recruits) {
            recruit.setFiles(recruitFileMapper.selectRecruitFiles(recruit));
        }
        return new PageInfo<>(recruits);
    }

    public void deleteRecruit(Integer id) {
        recruitMapper.deleteRecruit(id);
        recruitLabelMapper.deleteRecruitLabel(id);
        recruitFileMapper.deleteRecruitFile(id);
    }

    public void addJoinRequest(Integer recruitID, JoinRequest joinRequest) {
        Recruit recruit = recruitMapper.getRecruitById(recruitID); // 需要通过recruitID来得到team_id
        joinRequest.setTeamId(recruit.getTeamID());
        joinRequest.setCreateTime(LocalDateTime.now());
        joinRequest.setUpdateTime(LocalDateTime.now());
        recruitMapper.addJoinRequest(recruitID);
        joinRequestMapper.insertJoinRequest(joinRequest);
    }

    @Override
    public void insertRecruitFiles(Recruit recruit) {
        recruitFileMapper.insertRecruitFiles(recruit);
    }
}
