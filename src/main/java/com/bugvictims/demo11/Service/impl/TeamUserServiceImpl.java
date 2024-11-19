package com.bugvictims.demo11.Service.impl;

import com.bugvictims.demo11.Mapper.TeamUserMapper;
import com.bugvictims.demo11.Pojo.TeamUser;
import com.bugvictims.demo11.Service.TeamUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamUserServiceImpl implements TeamUserService {

    @Autowired
    private TeamUserMapper teamUserMapper;

    @Override
    public TeamUser findTeamUserByTeamIdAndUserId(int teamId, int userId) {
        return teamUserMapper.findTeamUserByTeamIdAndUserId(teamId, userId);
    }

    @Override
    public void addTeamUser(int teamId, int userId, String type) {
        TeamUser teamUser = new TeamUser();
        teamUser.setTeamId(teamId);
        teamUser.setUserId(userId);
        teamUser.setType(type);
        teamUserMapper.addTeamUser(teamUser);
    }

    @Override
    public boolean isTeamMember(int teamId, int userId) {
        TeamUser teamUser = teamUserMapper.findTeamUserByTeamIdAndUserId(teamId, userId);
        if (teamUser == null) {
            return false;
        }
        return "leader".equals(teamUser.getType()) || "member".equals(teamUser.getType());
    }

    @Override
    public boolean isTeamLeader(int teamId, int userId) {
        TeamUser teamUser = teamUserMapper.findTeamUserByTeamIdAndUserId(teamId, userId);
        return teamUser != null && "leader".equals(teamUser.getType());
    }

    @Override
    public List<TeamUser> findTeamUsersByTeamId(int teamId) {
        return teamUserMapper.findTeamUsersByTeamId(teamId);
    }

    @Override
    public void updateTeamUser(TeamUser teamUser) {
        teamUserMapper.updateTeamUser(teamUser);
    }

    @Override
    public void deleteTeamUser(int teamId, int userId) {
        teamUserMapper.deleteTeamUser(teamId, userId);
    }
}
