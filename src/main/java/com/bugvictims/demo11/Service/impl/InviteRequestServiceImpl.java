package com.bugvictims.demo11.Service.impl;
import com.bugvictims.demo11.Mapper.InviteRequestMapper;
import com.bugvictims.demo11.Pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class InviteRequestServiceImpl {
    @Autowired
    private InviteRequestMapper inviteRequestMapper;
    @Override
    public InviteRequest getInviteRequestById(int requestId) {
        return inviteRequestMapper.getInviteRequestByReleaserId(requestId);
    }

}
