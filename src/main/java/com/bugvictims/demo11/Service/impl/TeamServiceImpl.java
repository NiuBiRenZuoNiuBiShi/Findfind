package com.bugvictims.demo11.Service.impl;

import com.bugvictims.demo11.Mapper.TeamMapper;
import com.bugvictims.demo11.Mapper.TeamUserMapper;
import com.bugvictims.demo11.Pojo.Team;
import com.bugvictims.demo11.Pojo.TeamUser;
import com.bugvictims.demo11.Pojo.User;
import com.bugvictims.demo11.Service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private TeamUserMapper teamUserMapper;

    @Override
    public void createTeam(Team team, User loginUser) {

        //请求参数不能为空
        if(team==null){
            throw new RuntimeException("队伍信息不能为空");
        }

        //是否存在登录用户
        if(loginUser==null){
            throw new RuntimeException("未登录");
        }

        //创建者id
        Integer id = Integer.valueOf(loginUser.getid());
        //创建队伍
        teamMapper.createTeam(team);

        //添加队伍成员
        TeamUser teamUser = new TeamUser();
        teamUser.setTeamId(team.getId());
        teamUser.setUserId(id);
        teamUser.setType("leader");//设置为队长
        teamUserMapper.addTeamUser(teamUser);//设置创建者为队伍的成员
    }
}
