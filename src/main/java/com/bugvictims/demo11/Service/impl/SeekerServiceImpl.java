package com.bugvictims.demo11.Service.impl;

import com.bugvictims.demo11.Mapper.*;
import com.bugvictims.demo11.Pojo.InviteRequest;
import com.bugvictims.demo11.Pojo.Label;
import com.bugvictims.demo11.Pojo.PojoFile;
import com.bugvictims.demo11.Pojo.Seeker;
import com.bugvictims.demo11.Service.SeekerService;
import com.bugvictims.demo11.Utils.ThreadLocalUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeekerServiceImpl implements SeekerService {

    @Autowired
    private SeekerMapper seekerMapper;

    @Autowired
    private SeekerLabelMapper seekerLabelMapper;

    @Autowired
    private InviteRequestMapper inviteRequestMapper;

    @Autowired
    private SeekerFileMapper seekerFileMapper;

    @Autowired
    private InviteRequestFileMapper inviteRequestFileMapper;

    public Integer insertSeeker(Seeker seeker) {
        seekerMapper.createSeeker(seeker);
        seekerLabelMapper.insertSeekerLabel(seeker);
        return seeker.getId();
    }

    public boolean deleteSeeker(Integer id) {
        Map<String, Object> userClaims = ThreadLocalUtil.get();
        int userID = Integer.parseInt(userClaims.get("userID").toString());
        if (userID != seekerMapper.selectUserIDBySeekerID(userID)) {
            return false;
        }
        seekerMapper.deleteSeeker(id);
        seekerLabelMapper.deleteSeekerLabelBySeekerID(id);
        seekerFileMapper.deleteSeekerFile(id);
        return true;
    }

    public void updateSeeker(Seeker seeker) {
        seekerMapper.updateSeeker(seeker);
        seekerLabelMapper.deleteSeekerLabelBySeekerID(seeker.getId());
        seekerLabelMapper.insertSeekerLabel(seeker);
        seekerFileMapper.deleteSeekerFile(seeker.getId());
        seekerFileMapper.insertSeekerFile(seeker);
    }

    public PageInfo<Seeker> selectSeekers(List<String> labels, Integer page, Integer size) {
        List<Seeker> seekers = seekerMapper.selectSeekers(labels);
        List<Integer> seekerIDs = seekers.stream().map(Seeker::getId).toList();
        if (!seekerIDs.isEmpty()) {
            Map<Integer, List<Label>> labelsMap = seekerLabelMapper.selectSeekerLabelBySeekerIDs(seekerIDs).stream().collect(Collectors.groupingBy(Label::getSeekerId));
            seekers.forEach(seeker -> {
                seeker.setLabels(labelsMap.get(seeker.getId()).stream().map(Label::getLabel).collect(Collectors.toList()));
            });
        }
        return new PageInfo<>(seekers);
    }

    public Integer insertInviteRequest(InviteRequest inviteRequest, Integer seekerID) {
        inviteRequest.setCreateTime(LocalDateTime.now());
        inviteRequest.setUpdateTime(LocalDateTime.now()); // time
        Seeker seeker = seekerMapper.selectSeekerById(seekerID);
        inviteRequest.setUserID(seeker.getSeekerId()); // specific invited user
        //不重复插入
        if (inviteRequestMapper.getInviteRequestByTeamIdAndUserId(inviteRequest.getTeamID(), seekerID) != null) {
            return -1;
        }
        return inviteRequestMapper.insertInviteRequest(inviteRequest);
    }

    public void insertSeekerFiles(Seeker seeker) {
        seekerFileMapper.insertSeekerFile(seeker);
    }

    @Override
    public void insertInviteFiles(InviteRequest inviteRequest) {
        inviteRequestFileMapper.insertInviteFile(inviteRequest);
    }

    @Override
    public List<Seeker> getSeekersByUserId(Integer userID) {
        List<Seeker> seekers = seekerMapper.selectSeekersByUserID(userID);
        List<Integer> seekerIDs = seekers.stream().map(Seeker::getId).toList();
        if (!seekerIDs.isEmpty()) {
            Map<Integer, List<Label>> labelsMap = seekerLabelMapper.selectSeekerLabelBySeekerIDs(seekerIDs).stream().collect(Collectors.groupingBy(Label::getSeekerId));
            seekers.forEach(seeker -> {
                Optional<List<Label>> labelsOptional = Optional.ofNullable(labelsMap.get(seeker.getId()));
                labelsOptional.ifPresent(labels -> {
                    seeker.setLabels(labels.stream().map(Label::getLabel).collect(Collectors.toList()));
                });
            });
        }
        return seekers;
    }

    @Override
    public List<PojoFile> selectSeekerFilesBySeekerId(Integer seekerId) {
        return seekerFileMapper.selectSeekerFileBySeekerId(seekerId);
    }

}
