package com.bugvictims.demo11.Controller;

import com.bugvictims.demo11.Pojo.JoinRequest;
import com.bugvictims.demo11.Pojo.Result;
import com.bugvictims.demo11.Pojo.User;
import com.bugvictims.demo11.Service.JoinRequestService;
import com.bugvictims.demo11.Service.TeamService;
import com.bugvictims.demo11.Service.TeamUserService;
import com.bugvictims.demo11.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/join")
public class JoinController {

    @Autowired
    private JoinRequestService joinRequestService;

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamUserService teamUserService;

    //创建加入请求
    @PostMapping("/create/{teamId}/{userId}/{message}")
    public Result CreateJoinRequest(@PathVariable("teamId") int teamId, @PathVariable("userId") int userId, @PathVariable("message") String message) {

        User user = userService.getUserById(userId);
        if (user == null) {
            return new Result().error("用户不存在");
        }

        if (teamService.getTeamById(teamId) == null) {
            return new Result().error("队伍不存在");
        }

        if (teamUserService.isTeamMember(teamId, userId)) {
            return new Result().error("已经是队伍成员");
        }

        if (joinRequestService.getJoinRequestByTeamIdAndUserId(teamId, userId) != null) {
            return new Result().error("已经发送过请求");
        }
        joinRequestService.createJoinRequest(teamId, userId, message);
        return new Result().success();
    }

    //获取加入请求
    @PostMapping("/get/{requestId}")
    public Result getJoinRequest(@PathVariable("requestId") int requestId) {

        User loginUser = userService.getLoginUser();
        if (loginUser == null) {
            return new Result().error("未登录");
        }
        JoinRequest joinRequest = joinRequestService.getJoinRequestById(requestId);

        if (joinRequest == null) {
            return new Result().error("请求不存在");
        }


        if (!teamUserService.isTeamLeader(joinRequest.getTeamId(), loginUser.getId())
                && joinRequest.getUserId() != loginUser.getId()) {
            return new Result().error("无权限");
        }
        return new Result().success(joinRequest);
    }

    //处理加入请求
    @PostMapping("/handle/{requestId}/{statue}/{response}")
    public Result handleJoinRequest(@PathVariable("requestId") int requestId,
                                    @PathVariable(value = "statue") int statue,
                                    @PathVariable(value = "response", required = false) String response) {
        //判断是否为请求队伍的队长
        User loginUser = userService.getLoginUser();
        if (loginUser == null) {
            return new Result().error("未登录");
        }
        if (!teamUserService.isTeamLeader(joinRequestService
                .getJoinRequestById(requestId).getTeamId(), loginUser.getId())) {
            return new Result().error("无权限");
        }

        if (joinRequestService.isHandled(requestId)) {
            return new Result().error("已处理过");
        }

        joinRequestService.handleJoinRequest(requestId, statue, response);

        JoinRequest joinRequest = joinRequestService.getJoinRequestById(requestId);

        if (statue == 1) {
            teamService.joinTeam(joinRequest.getTeamId(), userService.getUserById(joinRequest.getUserId()));
        } else if (statue == 2) {
            return new Result().success("拒绝成功");
            //拒绝
        } else {
            return new Result().error("参数错误");
        }
        return new Result().success("加入成功");
    }

}
