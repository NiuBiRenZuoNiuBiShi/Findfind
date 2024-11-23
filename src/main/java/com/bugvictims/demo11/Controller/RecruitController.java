package com.bugvictims.demo11.Controller;

import com.bugvictims.demo11.Pojo.JoinRequest;
import com.bugvictims.demo11.Pojo.Recruit;
import com.bugvictims.demo11.Pojo.Result;
import com.bugvictims.demo11.Service.RecruitService;
import com.bugvictims.demo11.Utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
public class RecruitController {

    @Autowired
    private RecruitService recruitService;

    @PostMapping("/plaza")
    public Result releaseRecruit(@RequestBody Recruit recruit) {
        Map<String, Object> userClaims = ThreadLocalUtil.get();
        recruit.setReleaserID((int)userClaims.get("userID"));
        recruitService.releaseRecruit(recruit); //调用Service
        return new Result().success();
    }

    @PutMapping("/plaza")
    public Result updateRecruit(@RequestBody Recruit recruit) {
        recruit.setUpdateTime(LocalDateTime.now());
        recruitService.updateRecruit(recruit);
        return new Result().success();
    }

    @GetMapping("/plaza") // 通过label来筛选，所需要的Recruit，分页查询
    public Result getRecruits(@RequestParam List<String> labels
    , @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return new Result().success(recruitService.getRecruits(labels, page, size));
    }

    @DeleteMapping("/plaza/{id}")
    public Result deleteRecruit(@PathVariable Integer id) {
        if (id < 0) {
            return new Result().error("id not legal");
        }
        recruitService.deleteRecruit(id);
        return new Result().success();
    }

    @PostMapping("/plaza/{recruitID}") // 传入recruitID,在Body里传入具体的内容
    public Result addJoinRequest(@PathVariable Integer recruitID
            , @RequestBody JoinRequest joinRequest) {
        Map<String, Object> userClaims = ThreadLocalUtil.get();
        joinRequest.setUserId((int)userClaims.get("userID"));
        recruitService.addJoinRequest(recruitID, joinRequest);
        return new Result().success();
    }
}
