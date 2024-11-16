package com.bugvictims.demo11.Controller;

import com.bugvictims.demo11.Pojo.Result;
import com.bugvictims.demo11.Pojo.Team;
import com.bugvictims.demo11.Pojo.User;
import com.bugvictims.demo11.Service.TeamService;
import com.bugvictims.demo11.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public Result createTeam(@RequestBody Team team) {
        if (team == null) {
            return new Result().error("队伍信息不能为空");
        }
        User loginUser = userService.getLoginUser();
        teamService.createTeam(team, loginUser);
        return new Result().success();
    }

    //修改队伍信息
    @PostMapping("/update")
    public Result updateTeam(@RequestBody Team team) {
        if (team == null) {
            return new Result().error("队伍信息不能为空");
        }
        User loginUser = userService.getLoginUser();
        teamService.updateTeam(team, loginUser);
        return new Result().success();
    }

    //获取队伍信息By id
    @GetMapping("/get")
    public Result getTeamById(int id) {
        if (id <= 0) {
            return new Result().error("队伍id不合法");
        }
        Team team = teamService.getTeamById(id);
        if (team == null) {
            return new Result().error("队伍不存在");
        }
        return new Result().success(team);
    }


}
