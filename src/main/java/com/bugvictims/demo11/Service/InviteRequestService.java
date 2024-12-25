package com.bugvictims.demo11.Service;
import com.bugvictims.demo11.Pojo.InviteRequest;
import com.bugvictims.demo11.Pojo.JoinRequest;

public interface InviteRequestService {
     //获取请求
     InviteRequest getInviteRequestById(int requestId);
    //判断是否处理过
    boolean isHandled(int requestId);
    //处理请求
    void handleInviteRequest(int requestId, int statue, String response);

}
