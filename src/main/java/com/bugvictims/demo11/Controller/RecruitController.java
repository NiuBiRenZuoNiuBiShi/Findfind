package com.bugvictims.demo11.Controller;

import com.bugvictims.demo11.Pojo.Recruit;
import com.bugvictims.demo11.Pojo.Result;
import com.bugvictims.demo11.Service.RecruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class RecruitController {

    @Autowired
    private RecruitService recruitService;

    @PostMapping("plaza")
    public Result releaseRecruit(@RequestBody Recruit recruit) {
        recruit.setCreateTime(LocalDateTime.now());
        recruit.setUpdateTime(LocalDateTime.now());

        recruitService.releaseRecruit(recruit);

        return new Result().success();
    }
}
