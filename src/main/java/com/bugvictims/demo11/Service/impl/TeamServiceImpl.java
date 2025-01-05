package com.bugvictims.demo11.Service.impl;

import com.bugvictims.demo11.Mapper.TeamMapper;
import com.bugvictims.demo11.Pojo.Team;
import com.bugvictims.demo11.Pojo.TeamUser;
import com.bugvictims.demo11.Pojo.User;
import com.bugvictims.demo11.Service.TeamService;
import com.bugvictims.demo11.Service.TeamUserService;
import com.bugvictims.demo11.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private TeamUserService teamUserService;


    @Autowired
    private UserService userService;

    @Override
    public void createTeam(Team team, User loginUser) {


        //创建者id
        int id = loginUser.getId();
        //创建队伍
        teamMapper.createTeam(team);
        int teamId = teamMapper.getTeamByName(team.getName()).getId();
        //添加队伍成员为队长
        teamUserService.addTeamUser(teamId, id, "leader");
    }

    @Override
    public void updateTeam(Team team, User loginUser) {
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
        int count = teamMapper.getTeamUserCount(teamId);
        if (count == 1) {
            teamUserService.deleteTeamUser(teamId, userId);
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

    @Override
    public void deleteTeam(int teamId, User loginUser) {
        int userId = loginUser.getId();

        //检测用户身份
        if (!teamUserService.isTeamLeader(teamId, userId)) {
            throw new RuntimeException("权限不足，无法删除队伍");
        }
        //获取队伍成员
        List<TeamUser> teamUsers = teamUserService.findTeamUsersByTeamId(teamId);
        for (TeamUser teamUser : teamUsers) {
            //删除队伍成员
            teamUserService.deleteTeamUser(teamId, teamUser.getUserId());
        }

        //删除队伍
        teamMapper.deleteTeam(teamId);
    }

    @Override
    public List<User> listTeamUsers(int teamId) {
        //获取队伍成员
        List<TeamUser> teamUsers = teamUserService.findTeamUsersByTeamId(teamId);

        List<User> users = new ArrayList<>();
        for (TeamUser teamUser : teamUsers) {
            int userId = teamUser.getUserId();
            //查询用户信息
            User user = userService.getUserById(userId);
            users.add(user);
        }
        return users;
    }

    @Override
    public List<Team> listTeams() {
        //获取队伍列表
        return teamMapper.listTeams();
    }

    @Override
    public void joinTeam(int teamId, User loginUser) {
        int userId = loginUser.getId();
        //检测用户是否已加入队伍
        if (teamUserService.isTeamMember(teamId, userId)) {
            throw new RuntimeException("已加入队伍，无法重复加入");
        }
        //检测队伍人数
        int count = teamMapper.getTeamUserCount(teamId);
        if (count >= 5) {
            throw new RuntimeException("队伍人数已满，无法加入");
        }

        //加入队伍
        teamUserService.addTeamUser(teamId, userId, "member");
    }

    @Override
    public Team getTeamByName(String name) {
        return teamMapper.getTeamByName(name);
    }

    @Override
    public List<Team> getTeamByUser(User loginUser) {
        int userId = loginUser.getId();
        //获取用户队伍信息
        List<Integer> teamIds = teamMapper.findTeamIdsByUserId(userId);
        List<Team> teams = new ArrayList<>();
        for (int teamId : teamIds) {
            Team team = teamMapper.getTeamById(teamId);
            teams.add(team);
        }
        return teams;
    }

    @Override
    public void kickUser(int teamId, int userId) {
        if (!teamUserService.isTeamMember(teamId, userId)) {
            throw new RuntimeException("不是队伍成员，无法退出队伍");
        }

        teamUserService.deleteTeamUser(teamId, userId);
    }
}
