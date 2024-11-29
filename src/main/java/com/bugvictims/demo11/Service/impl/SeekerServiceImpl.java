package com.bugvictims.demo11.Service.impl;

import com.bugvictims.demo11.Mapper.*;
import com.bugvictims.demo11.Pojo.InviteRequest;
import com.bugvictims.demo11.Pojo.Seeker;
import com.bugvictims.demo11.Service.SeekerService;
import com.github.pagehelper.PageInfo;
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

    @Autowired
    private SeekerFileMapper seekerFileMapper;

    @Autowired
    private InviteRequestFileMapper inviteRequestFileMapper;

    public Integer insertSeeker(Seeker seeker) {
        Integer seekerID = seekerMapper.createSeeker(seeker);
        seekerLabelMapper.insertSeekerLabel(seeker);
        return seekerID;
    }

    public void deleteSeeker(Integer id) {
        seekerMapper.deleteSeeker(id);
        seekerLabelMapper.deleteSeekerLabelBySeekerID(id);
        seekerFileMapper.deleteSeekerFile(id);
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
        for (Seeker seeker : seekers) {
            seeker.setFiles(seekerFileMapper.selectSeekerFile(seeker));
        }
        return new PageInfo<>(seekers);
    }

    public Integer insertInviteRequest(InviteRequest inviteRequest, Integer seekerID) {
        inviteRequest.setCreateTime(LocalDateTime.now());
        inviteRequest.setUpdateTime(LocalDateTime.now()); // time
        Seeker seeker = seekerMapper.selectSeekerById(seekerID);
        inviteRequest.setUserID(seeker.getSeekerID()); // specific invited user

        return inviteRequestMapper.insertInviteRequest(inviteRequest);
    }

    public void insertSeekerFiles(Seeker seeker) {
        seekerFileMapper.insertSeekerFile(seeker);
    }

    @Override
    public void insertInviteFiles(InviteRequest inviteRequest) {
        inviteRequestFileMapper.insertInviteFile(inviteRequest);
    }
}
