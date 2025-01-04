package com.bugvictims.demo11.Controller;

import com.bugvictims.demo11.Pojo.Result;
import com.bugvictims.demo11.Pojo.User;
import com.bugvictims.demo11.Service.TeamUserService;
import com.bugvictims.demo11.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teams")
public class TeamUserController {

    @Autowired
    private TeamUserService teamUserService;

    @Autowired
    private UserService userService;

    @GetMapping("/isTeamLeader/{teamId}")
    public Result isTeamLeader(@PathVariable("teamId") int teamId) {
        User loginUser = userService.getLoginUser();
        int userId = loginUser.getId();
        return new Result().success(teamUserService.isTeamLeader(teamId, userId));
    }

    @GetMapping("/isTeamMember/{teamId}/{userId}")
    public Result isTeamMember(@PathVariable("teamId") int teamId, @PathVariable("userId") int userId) {
        return new Result().success(teamUserService.isTeamLeader(teamId, userId));
    }
}
