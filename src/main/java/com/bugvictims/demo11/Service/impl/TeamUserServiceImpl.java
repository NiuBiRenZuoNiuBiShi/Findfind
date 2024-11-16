package com.bugvictims.demo11.Service.impl;

import com.bugvictims.demo11.Mapper.TeamUserMapper;
import com.bugvictims.demo11.Pojo.TeamUser;
import com.bugvictims.demo11.Service.TeamUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamUserServiceImpl implements TeamUserService {

    @Autowired
    private TeamUserMapper teamUserMapper;

    @Override
    public TeamUser findTeamUserByTeamIdAndUserId(int teamId, int userId) {
       return teamUserMapper.findTeamUserByTeamIdAndUserId(teamId, userId);
    }

    @Override
    public void addTeamUser(int teamId, int userId,String type) {
        TeamUser teamUser = new TeamUser();
        teamUser.setTeamId(teamId);
        teamUser.setUserId(userId);
        teamUser.setType(type);
        teamUserMapper.addTeamUser(teamUser);
    }

    @Override
    public boolean isTeamAdminOrLeader(int teamId, int userId) {
        TeamUser teamUser = teamUserMapper.findTeamUserByTeamIdAndUserId(teamId, userId);
        if (teamUser == null) {
            return false;
        }
        return "admin".equals(teamUser.getType()) || "leader".equals(teamUser.getType());
    }
}
