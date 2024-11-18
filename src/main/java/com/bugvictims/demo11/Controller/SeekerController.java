package com.bugvictims.demo11.Controller;

import com.bugvictims.demo11.Pojo.InviteRequest;
import com.bugvictims.demo11.Pojo.Result;
import com.bugvictims.demo11.Pojo.Seeker;
import com.bugvictims.demo11.Service.SeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class SeekerController {

    @Autowired
    SeekerService seekerService;

    @PostMapping("/plaza/seeker")
    Result createSeeker(@RequestBody Seeker seeker, @RequestAttribute("userID") int userID) {
        seeker.setId(userID);
        seeker.setCreateTime(LocalDateTime.now());
        seeker.setUpdateTime(LocalDateTime.now());
        seekerService.createSeeker(seeker);
        return new Result().success();
    }

    @DeleteMapping("/plaza/seeker/{id}")
    Result deleteSeeker(@PathVariable Integer id) {
        seekerService.deleteSeeker(id);
        return new Result().success();
    }

    @PutMapping("/plaza/seeker")
    Result updateSeeker(@RequestBody Seeker seeker) {
        seeker.setUpdateTime(LocalDateTime.now());
        seekerService.updateSeeker(seeker);
        return new Result().success();
    }

    @GetMapping("/plaza/seeker")
    Result selectSeeker(@RequestParam("label") List<String> labels) {
        return new Result().success(seekerService.getSeekers(labels));
    }

    @PostMapping("/plaza/seeker/invite/{seekerID}/{teamID}")
    Result inviteSeeker(@RequestBody InviteRequest inviteRequest
            , @RequestAttribute("userID") int userID
            , @PathVariable("seekerID") Integer seekerID
            , @PathVariable("teamID") Integer teamID )  {
        inviteRequest.setReleaserID(userID);
        inviteRequest.setTeamID(teamID);
        seekerService.inviteSeeker(inviteRequest, seekerID);
        return new Result().success();
    }
}
