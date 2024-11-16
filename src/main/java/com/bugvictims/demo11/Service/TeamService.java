package com.bugvictims.demo11.Service;

import com.bugvictims.demo11.Pojo.Team;
import com.bugvictims.demo11.Pojo.User;

public interface TeamService {

    //创建队伍
    void createTeam(Team team, User loginUser);

    //更新队伍
    void updateTeam(Team team, User loginUser);

    //获取队伍信息By id
    Team getTeamById(int id);
}
