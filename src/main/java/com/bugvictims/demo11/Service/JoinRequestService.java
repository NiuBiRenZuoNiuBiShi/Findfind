package com.bugvictims.demo11.Service;

import com.bugvictims.demo11.Pojo.JoinRequest;
import com.github.pagehelper.PageInfo;

public interface JoinRequestService {

    //查找处理请求By teamId and userId
    JoinRequest getJoinRequestByTeamIdAndUserId(int teamId, int userId);

    //创建加入队伍的请求
    void createJoinRequest(int teamId, int userId, String message);

    //获取加入请求
    JoinRequest getJoinRequestById(int requestId);

    //判断是否处理过
    boolean isHandled(int requestId);

    //处理加入请求
    void handleJoinRequest(int requestId, int statue, String response);

    //删除加入请求
    void deleteJoinRequest(int requestId);

    //获取别人的加入请求
    PageInfo<JoinRequest> getJoins(int id, Integer page, Integer size, int teamId);

    //获取所有加入请求
    PageInfo<JoinRequest> getJoinsAll(int id, Integer page, Integer size);
}
