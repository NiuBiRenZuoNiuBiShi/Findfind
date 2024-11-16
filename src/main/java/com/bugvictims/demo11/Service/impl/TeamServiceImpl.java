package com.bugvictims.demo11.Service.impl;

import com.bugvictims.demo11.Mapper.TeamMapper;
import com.bugvictims.demo11.Pojo.Team;
import com.bugvictims.demo11.Pojo.TeamUser;
import com.bugvictims.demo11.Pojo.User;
import com.bugvictims.demo11.Service.TeamService;
import com.bugvictims.demo11.Service.TeamUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private TeamUserService teamUserService;
    @Autowired
    private TeamService teamService;

    @Override
    public void createTeam(Team team, User loginUser) {

        //请求参数不能为空
        if (team == null) {
            throw new RuntimeException("队伍信息不能为空");
        }

        //是否存在登录用户
        if (loginUser == null) {
            throw new RuntimeException("未登录");
        }

        //创建者id
        int id = loginUser.getId();
        //创建队伍
        teamMapper.createTeam(team);

        //添加队伍成员为队长
        teamUserService.addTeamUser(team.getId(), id, "leader");
    }

    @Override
    public void updateTeam(Team team, User loginUser) {

        //请求参数不能为空
        if (team == null) {
            throw new RuntimeException("队伍信息不能为空");
        }

        //是否存在登录用户
        if (loginUser == null) {
            throw new RuntimeException("未登录");
        }

        //检测用户身份
        if (!teamUserService.isTeamAdminOrLeader(team.getId(), loginUser.getId())) {
            throw new RuntimeException("权限不足，无法修改队伍信息");
        }

        //更新队伍信息
        teamMapper.updateTeam(team);
    }

    @Override
    public Team getTeamById(int id) {
        return teamMapper.getTeamById(id);
    }

    @Override
    public int getTeamUserCount(int teamId) {
        return teamMapper.getTeamUserCount(teamId);
    }

    @Override
    public void quitTeam(int teamId, User loginUser) {
        int userId = loginUser.getId();

        //检测用户身份
        if (!teamUserService.isTeamMember(teamId, userId)) {
            throw new RuntimeException("不是队伍成员，无法退出队伍");
        }

        //检测队伍人数
        int count = teamService.getTeamUserCount(teamId);

        if (count == 1) {
            //队伍只有一个人，删除队伍
            teamMapper.deleteTeam(teamId);
        } else {
            //退出队伍
            if (teamUserService.isTeamLeader(teamId, userId)) {
                //队长退出，设置队伍新队长
                List<TeamUser> teamUsers = teamUserService.findTeamUsersByTeamId(teamId);
                for (TeamUser teamUser : teamUsers) {
                    if (!teamUser.getType().equals("leader")) {
                        teamUser.setType("leader");
                        teamUserService.updateTeamUser(teamUser);
                        break;
                    }
                }
            }
            //删除队伍成员
            teamUserService.deleteTeamUser(teamId, userId);
        }
    }
}
