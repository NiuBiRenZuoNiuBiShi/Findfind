package com.bugvictims.demo11.Controller;

import com.bugvictims.demo11.Pojo.JoinRequest;
import com.bugvictims.demo11.Pojo.Result;
import com.bugvictims.demo11.Pojo.Team;
import com.bugvictims.demo11.Pojo.User;
import com.bugvictims.demo11.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @Autowired
    private TeamUserService teamUserService;

    @Autowired
    private RecruitService recruitService;

    @Autowired
    private JoinRequestService joinRequestService;

    @PostMapping("/create")
    public Result createTeam(@RequestBody Team team) {
        if (team == null) {
            return new Result().error("队伍信息不能为空");
        }

        String name = team.getName();
        if (teamService.getTeamByName(name) != null) {
            return new Result().error("队伍名称已存在");
        }

        User loginUser = userService.getLoginUser();

        //是否存在登录用户
        if (loginUser == null) {
            throw new RuntimeException("未登录");
        }

        teamService.createTeam(team, loginUser);

        return new Result().success();
    }

    //修改队伍信息
    @PostMapping("/update")
    public Result updateTeam(@RequestBody Team team) {
        if (team == null) {
            return new Result().error("队伍信息不能为空");
        }
        if (teamService.getTeamByName(team.getName()) != null) {
            return new Result().error("队伍名称已存在");
        }
        User loginUser = userService.getLoginUser();

        //是否存在登录用户
        if (loginUser == null) {
            throw new RuntimeException("未登录");
        }

        //检测用户身份
        if (!teamUserService.isTeamLeader(team.getId(), loginUser.getId())) {
            throw new RuntimeException("权限不足，无法修改队伍信息");
        }

        teamService.updateTeam(team, loginUser);
        return new Result().success();
    }

    //获取队伍信息By id
    @GetMapping("/get/{id}")
    public Result getTeamById(@PathVariable("id") int id) {
        if (id <= 0) {
            return new Result().error("队伍id不合法");
        }
        Team team = teamService.getTeamById(id);
        if (team == null || team.getStatus() == 3) {
            return new Result().error("队伍不存在");
        }
        return new Result().success(team);
    }

    //退出队伍
    @PostMapping("/quit/{teamId}")
    public Result quitTeam(@PathVariable("teamId") int teamId) {
        if (teamId <= 0) {
            return new Result().error("队伍id不合法");
        }
        User loginUser = userService.getLoginUser();
        teamService.quitTeam(teamId, loginUser);
        return new Result().success();
    }

    //删除队伍
    @PostMapping("/delete/{teamId}")
    public Result deleteTeam(@PathVariable("teamId") int teamId) {
        if (teamId <= 0) {
            return new Result().error("队伍id不合法");
        }
        User loginUser = userService.getLoginUser();
        teamService.deleteTeam(teamId, loginUser);
        return new Result().success();
    }

    //获取队伍信息
    @GetMapping("/list")
    public Result listTeams() {
        List<Team> teamList = teamService.listTeams();
        if (teamList == null || teamList.isEmpty()) {
            return new Result().error("队伍列表为空");
        }
        return new Result().success(teamList);
    }

    //加入队伍
    @PostMapping("/join/{teamId}")
    public Result joinTeam(@PathVariable("teamId") int teamId) {
        if (teamId <= 0) {
            return new Result().error("队伍id不合法");
        }
        User loginUser = userService.getLoginUser();
        teamService.joinTeam(teamId, loginUser);
        return new Result().success();
    }

    //获取队伍成员
    @GetMapping("/listUsers/{teamId}")
    public Result listTeamUsers(@PathVariable("teamId") int teamId) {
        if (teamId <= 0) {
            return new Result().error("队伍id不合法");
        }
        List<User> userList = teamService.listTeamUsers(teamId);
        if (userList == null || userList.isEmpty()) {
            return new Result().error("队伍成员为空");
        }
        return new Result().success(userList);
    }

    @GetMapping("/teams/recruit/{teamID}")
    public Result getRecruitsByTeamId(@PathVariable Integer teamID) {
        return new Result().success(recruitService.getRecruitsByTeamId(teamID));
    }

    @GetMapping("teams/join/{recruitID}")
    public Result getJoinByRecruitID(@PathVariable Integer recruitID) {
        return new Result().success(joinRequestService.getJoinRequestById(recruitID));
    }
}
