package com.bugvictims.demo11.Controller;

import com.bugvictims.demo11.Pojo.JoinRequest;
import com.bugvictims.demo11.Pojo.Recruit;
import com.bugvictims.demo11.Pojo.Result;
import com.bugvictims.demo11.Service.RecruitService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class RecruitController {

    @Autowired
    private RecruitService recruitService;

    @PostMapping("plaza")
    public Result releaseRecruit(@RequestBody Recruit recruit // 前端传入的数据，会自动绑定到这个对象上
            , @RequestAttribute("userID") int userID) { // 拦截器拦截放行后，可以得到这个
        recruit.setReleaserID(userID);
        recruitService.releaseRecruit(recruit); //调用Service
        return new Result().success();
    }

    @PutMapping("plaza")
    public Result updateRecruit(@RequestBody Recruit recruit) {
        recruit.setUpdateTime(LocalDateTime.now());
        recruitService.updateRecruit(recruit);
        return new Result().success();
    }

    @GetMapping("plaza") // 通过label来筛选，所需要的Recruit，分页查询
    public Result getRecruits(@RequestParam List<String> labels
    , @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        return new Result().success(recruitService.getRecruits(labels, page, size));
    }

    @DeleteMapping("plaza/{id}")
    public Result deleteRecruit(@PathVariable int id) {
        recruitService.deleteRecruit(id);
        return new Result().success();
    }

    @PostMapping("plaza/{recruitID}") // 传入recruitID,在Body里传入具体的内容
    public Result addJoinRequest(@PathVariable int recruitID, @RequestBody JoinRequest joinRequest
            , @RequestAttribute("userID") int userID) {
        joinRequest.setUserId(userID);
        recruitService.addJoinRequest(recruitID, joinRequest);
        return new Result().success();
    }
}
