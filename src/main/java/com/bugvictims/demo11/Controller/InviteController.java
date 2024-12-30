package com.bugvictims.demo11.Controller;
import com.bugvictims.demo11.Pojo.InviteRequest;
import com.bugvictims.demo11.Pojo.Result;
import com.bugvictims.demo11.Pojo.User;
import com.bugvictims.demo11.Service.InviteRequestService;
import com.bugvictims.demo11.Service.TeamService;
import com.bugvictims.demo11.Service.TeamUserService;
import com.bugvictims.demo11.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invite")
public class InviteController {

    @Autowired
    private InviteRequestService inviteRequestService;
    @Autowired
    private UserService userService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private TeamUserService teamUserService;
    //创建邀请
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
        if (inviteRequestService.getInviteRequestByTeamIdAndUserId(teamId, userId) != null) {
            return new Result().error("已经发送过请求");
        }
        inviteRequestService.createInviteRequest(teamId, userId, message);
        return new Result().success();
    }

    //获取加入请求
    @GetMapping("/get/{requestId}")
    public Result getJoinRequest(@PathVariable("requestId") int requestId) {
        User loginUser = userService.getLoginUser();
        if (loginUser == null) {
            return new Result().error("未登录");
        }
        InviteRequest inviteRequest = inviteRequestService.getInviteRequestById(requestId);

        if (inviteRequest == null) {
            return new Result().error("请求不存在");
        }
        if (!teamUserService.isTeamLeader(inviteRequest.getTeamID(), loginUser.getId()) && inviteRequest.getUserID() != loginUser.getId()) {
            return new Result().error("无权限");
        }
        return new Result().success(inviteRequest);
    }

    //处理请求
    @PostMapping("/handle")
    public Result handleInviteRequest(@RequestParam Integer requestId,@RequestParam Integer status,@RequestParam String response){
        User loginUser = userService.getLoginUser();
        if (loginUser == null) {
            return new Result().error("未登录");
        }
        inviteRequestService.updateInviteRequestStatus(requestId, status,response);
        InviteRequest inviteRequest = inviteRequestService.getInviteRequestById(requestId);
        if (status == 1) {
            teamService.joinTeam(inviteRequest.getTeamID(), userService.getUserById(inviteRequest.getUserID()));
        } else if (status == 2) {
            return new Result().success("拒绝成功");
            //拒绝
        } else if(status==0) {
            return new Result().success("操作成功");
        }else{
            return new Result().error("参数错误");
        }
        return new Result().success("加入成功");
    }
    //删除加入请求(只有发布者可以删除)
    @PostMapping("/delete/{requestId}")
    public Result deleteJoinRequest(@PathVariable("requestId") int requestId) {
        User loginUser = userService.getLoginUser();
        if (loginUser == null) {
            return new Result().error("未登录");
        }
        InviteRequest inviteRequest = inviteRequestService.getInviteRequestById(requestId);
        if (inviteRequest == null) {
            return new Result().error("请求不存在");
        }
        if (inviteRequest.getUserID() != loginUser.getId()) {
            return new Result().error("无权限");
        }
        inviteRequestService.deleteInviteRequest(requestId);
        return new Result().success();
    }
}

