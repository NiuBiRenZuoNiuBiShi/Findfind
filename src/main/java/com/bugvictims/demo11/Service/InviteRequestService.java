package com.bugvictims.demo11.Service;
import com.bugvictims.demo11.Pojo.InviteRequest;
import com.bugvictims.demo11.Pojo.JoinRequest;

import java.util.List;

public interface InviteRequestService {
    //创建新邀请
    Integer insertInviteRequest(InviteRequest inviteRequest);

    void   createInviteRequest(int teamId, int userId, String message);
    //队伍发出所有邀请
    List<InviteRequest> getInviteRequestsByTeamId(Integer teamID);
    //用户收到所有邀请
    List<InviteRequest> getInviteRequestsByUserId(Integer userID);
    //id查找
    InviteRequest getInviteRequestById(Integer ID);
    //用户和队伍查找
    InviteRequest getInviteRequestByTeamIdAndUserId(int teamId, int userId);
   //处理邀请
    void updateInviteRequestStatus(int requestId, int statue, String response);
    //删除邀请
    void deleteInviteRequest(int requestId);

}
