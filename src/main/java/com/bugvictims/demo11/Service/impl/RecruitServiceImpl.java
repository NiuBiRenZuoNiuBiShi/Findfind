package com.bugvictims.demo11.Service.impl;

import com.bugvictims.demo11.Mapper.*;
import com.bugvictims.demo11.Pojo.JoinRequest;
import com.bugvictims.demo11.Pojo.Label;
import com.bugvictims.demo11.Pojo.PojoFile;
import com.bugvictims.demo11.Pojo.Recruit;
import com.bugvictims.demo11.Service.RecruitService;
import com.bugvictims.demo11.Utils.ThreadLocalUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private JoinRequestFileMapper joinRequestFileMapper;

    public Integer insertRecruit(Recruit recruit) {
        recruit.setCreateTime(LocalDateTime.now()); // 添加一些数据
        recruit.setUpdateTime(LocalDateTime.now());
        recruitMapper.insertRecruit(recruit); // 调用Mapper
        recruitLabelMapper.addRecruitLabel(recruit);
        return recruit.getId();
    }

    public void updateRecruit(Recruit recruit) {
        recruitMapper.updateRecruit(recruit);
        recruitLabelMapper.deleteRecruitLabel(recruit.getId());
        recruitLabelMapper.addRecruitLabel(recruit);
        recruitFileMapper.deleteRecruitFile(recruit.getId());
        recruitFileMapper.insertRecruitFiles(recruit);
    }

    public PageInfo<Recruit> selectRecruits(List<String> labels, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize); //分页查询...
        List<Recruit> recruits = recruitMapper.getRecruits(labels);
        List<Integer> recruitIds = recruits.stream()
                .map(Recruit::getId)
                .collect(Collectors.toList());
        if (!recruitIds.isEmpty()) {
            Map<Integer, List<Label>> labelsMap = recruitLabelMapper
                    .selectRecruitLabelByRecruitIds(recruitIds)
                    .stream()
                    .collect(Collectors.groupingBy(Label::getSeekerId));
            recruits.forEach(recruit -> {
                recruit.setLabels(labelsMap.get(recruit.getId()).stream().map(Label::getLabel).collect(Collectors.toList()));
            });
        }
        return new PageInfo<>(recruits);
    }

    public boolean deleteRecruit(Integer id) {
        Map<String, Object> UserClaims = ThreadLocalUtil.get();
        int userID = Integer.parseInt(UserClaims.get("userID").toString());
        if (userID != recruitMapper.selectUserIDByRecruitID(id)) {
            return false;
        }
        recruitMapper.deleteRecruit(id);
        recruitLabelMapper.deleteRecruitLabel(id);
        recruitFileMapper.deleteRecruitFile(id);
        return true;
    }

    public Integer insertJoinRequest(Integer recruitID, JoinRequest joinRequest) {
        Recruit recruit = recruitMapper.getRecruitById(recruitID); // 需要通过recruitID来得到team_id
        joinRequest.setTeamId(recruit.getTeamID());
        joinRequest.setCreateTime(LocalDateTime.now());
        joinRequest.setUpdateTime(LocalDateTime.now());
        recruitMapper.addJoinRequest(recruitID);
        return joinRequestMapper.insertJoinRequest(joinRequest);
    }

    @Override
    public void insertRecruitFiles(Recruit recruit) {
        recruitFileMapper.insertRecruitFiles(recruit);
    }

    @Override
    public void insertJoinFiles(JoinRequest joinRequest) {
        joinRequestFileMapper.insertJoinFile(joinRequest);
    }

    @Override
    public List<Recruit> getRecruitsByTeamId(Integer teamID) {
        List<Recruit> recruits = recruitMapper.selectRecruitsByTeamID(teamID);
        List<Integer> recruitIds = recruits.stream().map(Recruit::getId).toList();
        if (!recruitIds.isEmpty()) {
            Map<Integer, List<Label>> labelsMap = recruitLabelMapper
                    .selectRecruitLabelByRecruitIds(recruitIds)
                    .stream()
                    .collect(Collectors.groupingBy(Label::getSeekerId));
            recruits.forEach(recruit -> {
                recruit.setLabels(labelsMap.get(recruit.getId()).stream().map(Label::getLabel).collect(Collectors.toList()));
            });
        }
        return recruits;
    }

    @Override
    public List<PojoFile> selectRecruitFilesByItsId(Integer recruitId) {
        return recruitFileMapper.selectRecruitFilesByRecruitId(recruitId);
    }
}
