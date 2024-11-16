package com.bugvictims.demo11.Service;

import com.bugvictims.demo11.Pojo.Team;
import com.bugvictims.demo11.Pojo.User;

public interface TeamService {

    void createTeam(Team team, User loginUser);
}
