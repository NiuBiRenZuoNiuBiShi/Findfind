package com.bugvictims.demo11.Service;

import com.bugvictims.demo11.Pojo.JoinRequest;

public interface JoinRequestService {

    //创建加入队伍的请求
    void createJoinRequest(int teamId, int userId, String message);

    JoinRequest getJoinRequestById(int requestId);
}
