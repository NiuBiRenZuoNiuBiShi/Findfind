package com.bugvictims.demo11.Service;

import com.bugvictims.demo11.Pojo.Team;
import com.bugvictims.demo11.Pojo.User;

import java.util.List;

public interface TeamService {

    //创建队伍
    void createTeam(Team team, User loginUser);

    //更新队伍
    void updateTeam(Team team, User loginUser);

    //获取队伍信息By id
    Team getTeamById(int id);

    //获取队伍人数
    int getTeamUserCount(int teamId);

    //退出队伍
    void quitTeam(int teamId, User loginUser);

    //删除队伍
    void deleteTeam(int teamId, User loginUser);

    //获取队伍成员
    List<User> listTeamUsers(int teamId);

    //获取队伍列表
    List<Team> listTeams();

    //加入队伍
    void joinTeam(int teamId, User loginUser);

    //根据队伍名称获取队伍信息
    Team getTeamByName(String name);

    //根据用户获取队伍信息
    List<Team> getTeamByUser(User loginUser);
}
