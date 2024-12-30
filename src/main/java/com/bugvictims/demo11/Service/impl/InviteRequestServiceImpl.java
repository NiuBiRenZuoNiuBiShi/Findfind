package com.bugvictims.demo11.Service.impl;
import com.bugvictims.demo11.Mapper.InviteRequestMapper;
import com.bugvictims.demo11.Service.InviteRequestService;
import com.bugvictims.demo11.Pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class InviteRequestServiceImpl implements InviteRequestService {
    @Autowired
    private InviteRequestMapper inviteRequestMapper;
    @Override
    public Integer insertInviteRequest(InviteRequest inviteRequest) {
        return inviteRequestMapper.insertInviteRequest(inviteRequest);
    }
    @Override
    public void createInviteRequest(int teamId, int userId, String message){
        inviteRequestMapper.createInviteRequest(teamId,userId,message);
    }
     @Override
    public List<InviteRequest> getInviteRequestsByTeamId(Integer teamID){
         return inviteRequestMapper.getInviteRequestsByTeamId(teamID);
    }
    @Override
    public  List<InviteRequest> getInviteRequestsByUserId(Integer userID){
        return inviteRequestMapper.getInviteRequestsByUserId(userID);
    }
    @Override
    public InviteRequest getInviteRequestById(Integer ID){
         return inviteRequestMapper.getInviteRequestById(ID);
    }
    @Override
    public InviteRequest getInviteRequestByTeamIdAndUserId(int teamId, int userId){
         return inviteRequestMapper.getInviteRequestByTeamIdAndUserId(teamId,userId);
    }
    @Override
    public void updateInviteRequestStatus(int requestId, int statue, String response){
        inviteRequestMapper.updateInviteRequestStatus(requestId,statue,response);
    }
    @Override
    public void deleteInviteRequest(int requestId){
        inviteRequestMapper.deleteInviteRequest(requestId);
    }
}
