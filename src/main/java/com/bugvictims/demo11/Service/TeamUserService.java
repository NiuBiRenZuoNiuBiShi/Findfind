package com.bugvictims.demo11.Service;

import com.bugvictims.demo11.Pojo.TeamUser;

public interface TeamUserService {

    //查找团队成员
    TeamUser findTeamUserByTeamIdAndUserId(int teamId, int userId);

    // 添加团队成员
    void addTeamUser(int teamId, int userId,String type);

    //是否为管理员或队长
    boolean isTeamAdminOrLeader(int teamId, int userId);

}
