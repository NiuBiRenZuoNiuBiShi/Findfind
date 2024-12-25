package com.bugvictims.demo11.Controller;
import com.bugvictims.demo11.Service.InviteRequestService;
import com.bugvictims.demo11.Service.TeamService;
import com.bugvictims.demo11.Service.TeamUserService;
import com.bugvictims.demo11.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import com.bugvictims.demo11.Pojo.*;
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
    //获取加入请求
    @GetMapping("/get/{requestId}")
    public Result getInviteRequest(@PathVariable("requestId") int requestId) {
        User loginUser = userService.getLoginUser();
        if (loginUser == null) {
            return new Result().error("未登录");
        }
        InviteRequest inviteRequest = inviteRequestService.getInviteRequestById(requestId);
        if (inviteRequest == null){
            return new Result().error("请求不存在");
        }
        return new Result().success(inviteRequest);
    }

    // 个人处理邀请请求
    @PostMapping("/handleInvite/{requestId}/{status}/{response}")
    public Result handleInviteRequest(@PathVariable("requestId") int requestId,
                                      @PathVariable("status") int status, // 注意这里参数名使用status，而不是statue，以保持一致性
                                      @PathVariable(value = "response", required = false) String response) {
        // 获取当前登录用户
        User loginUser = userService.getLoginUser();
        if (loginUser == null) {
            return new Result().error("未登录");
        }
        // 检查请求是否存在且未被处理
        InviteRequest inviteRequest = inviteRequestService.getInviteRequestById(requestId);
        if (inviteRequest == null || inviteRequest.getStatus() != 0) {
            return new Result().error("请求不存在或已处理");
        }
        // 处理邀请请求
        inviteRequestService.handleInviteRequest(requestId, status, response);
        if (status == 1) {
            // 接受邀请，更新队伍成员关系
            teamService.addMember(inviteRequest.getTeamId(), loginUser);
        } else if (status == 2) {
            // 拒绝邀请，无需额外操作
        } else {
            return new Result().error("参数错误");
        }
        // 返回处理结果
        if (status == 1) {
            return new Result().success("接受邀请成功");
        } else {
            return new Result().success("拒绝邀请成功");
        }
    }
}

