package com.bugvictims.demo11.Service.impl;

import com.bugvictims.demo11.Mapper.InviteRequestMapper;
import com.bugvictims.demo11.Mapper.SeekerLabelMapper;
import com.bugvictims.demo11.Mapper.SeekerMapper;
import com.bugvictims.demo11.Pojo.InviteRequest;
import com.bugvictims.demo11.Pojo.Seeker;
import com.bugvictims.demo11.Service.SeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SeekerServiceImpl implements SeekerService {

    @Autowired
    private SeekerMapper seekerMapper;

    @Autowired
    private SeekerLabelMapper seekerLabelMapper;

    @Autowired
    private InviteRequestMapper inviteRequestMapper;

    public void createSeeker(Seeker seeker) {
        seekerMapper.createSeeker(seeker);
        seekerLabelMapper.insertSeekerLabel(seeker);
    }

    public void deleteSeeker(Integer id) {
        seekerMapper.deleteSeeker(id);
        seekerLabelMapper.deleteSeekerLabelBySeekerID(id);
    }

    public void updateSeeker(Seeker seeker) {
        seekerMapper.updateSeeker(seeker);
        seekerLabelMapper.deleteSeekerLabelBySeekerID(seeker.getId());
        seekerLabelMapper.insertSeekerLabel(seeker);
    }

    public List<Seeker> getSeekers(List<String> labels) {
        return seekerMapper.selectSeekers(labels);
    }

    public void inviteSeeker(InviteRequest inviteRequest, Integer seekerID) {
        inviteRequest.setCreateTime(LocalDateTime.now());
        inviteRequest.setUpdateTime(LocalDateTime.now()); // time
        Seeker seeker = seekerMapper.selectSeekerById(seekerID);
        inviteRequest.setUserID(seeker.getSeekerID()); // specific invited user
        inviteRequestMapper.insertInviteRequest(inviteRequest);
    }
}
