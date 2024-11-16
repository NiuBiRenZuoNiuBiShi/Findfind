package com.bugvictims.demo11.Service;

import com.bugvictims.demo11.Pojo.TeamUser;

import java.util.List;

public interface TeamUserService {

    //查找团队成员
    TeamUser findTeamUserByTeamIdAndUserId(int teamId, int userId);

    // 添加团队成员
    void addTeamUser(int teamId, int userId, String type);

    //是否为管理员或队长
    boolean isTeamAdminOrLeader(int teamId, int userId);

    //是否为组员
    boolean isTeamMember(int teamId, int userId);

    //是否为队长
    boolean isTeamLeader(int teamId, int userId);

    //查找团队成员
    List<TeamUser> findTeamUsersByTeamId(int teamId);

    //更新团队成员
    void updateTeamUser(TeamUser teamUser);

    //删除团队成员
    void deleteTeamUser(int teamId, int userId);
}
