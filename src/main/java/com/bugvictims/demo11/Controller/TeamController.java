package com.bugvictims.demo11.Controller;

import com.bugvictims.demo11.Pojo.Result;
import com.bugvictims.demo11.Pojo.Team;
import com.bugvictims.demo11.Pojo.User;
import com.bugvictims.demo11.Service.TeamService;
import com.bugvictims.demo11.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public Result createTeam(@RequestBody Team team){
        if(team == null){
            return new Result().error("队伍信息不能为空");
        }
        User loginUser = userService.getLoginUser();// 暂无实现
        teamService.createTeam(team, loginUser);
        return new Result().success();
    }

}
