package com.bugvictims.demo11.Service.impl;

import com.bugvictims.demo11.Mapper.JoinRequestMapper;
import com.bugvictims.demo11.Pojo.JoinRequest;
import com.bugvictims.demo11.Service.JoinRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class JoinRequestServiceImpl implements JoinRequestService {

    @Autowired
    private JoinRequestMapper joinRequestMapper;

    @Override
    public void createJoinRequest(int teamId, int userId, String message) {
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setTeamId(teamId);
        joinRequest.setUserId(userId);
        joinRequest.setMessage(message);
        joinRequest.setStatus(0);
        joinRequest.setCreateTime(LocalDateTime.now());
        joinRequest.setUpdateTime(LocalDateTime.now());
        joinRequestMapper.insertJoinRequest(joinRequest);
    }

    @Override
    public JoinRequest getJoinRequestById(int requestId) {
        return joinRequestMapper.getJoinRequestById(requestId);
    }
}
