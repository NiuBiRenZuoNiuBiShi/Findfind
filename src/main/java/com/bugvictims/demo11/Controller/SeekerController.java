package com.bugvictims.demo11.Controller;

import com.bugvictims.demo11.Pojo.InviteRequest;
import com.bugvictims.demo11.Pojo.Recruit;
import com.bugvictims.demo11.Pojo.Result;
import com.bugvictims.demo11.Pojo.Seeker;
import com.bugvictims.demo11.Service.SeekerService;
import com.bugvictims.demo11.Utils.FileConverter;
import com.bugvictims.demo11.Utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
public class SeekerController {

    @Autowired
    SeekerService seekerService;

    @PostMapping("/plaza/seeker")
    Result createSeeker(@ModelAttribute Seeker seeker
                        , @RequestParam(value = "labels", required = true) List<String> labels
                        , @RequestParam(value="files", required = false) List<MultipartFile> files) {
        Map<String, Object> userClaims = ThreadLocalUtil.get();
        if (userClaims == null) {
            return new Result().error("当前无用户登录");
        }
        seeker.setSeekerId((Integer) userClaims.get("userID"));
        if (labels != null) {
            seeker.setLabels(labels);
        }
        seeker.setCreateTime(LocalDateTime.now());
        seeker.setUpdateTime(LocalDateTime.now());
        Integer seekerID = seekerService.insertSeeker(seeker);
        if (files != null && !files.isEmpty()) {
            seeker.setFiles(FileConverter.convertToPojoFileList(files, seekerID));
            seekerService.insertSeekerFiles(seeker);
        }
        return new Result().success();
    }

    @DeleteMapping("/plaza/seeker/{id}")
    Result deleteSeeker(@PathVariable Integer id) {
        if (seekerService.deleteSeeker(id))
            return new Result().success();
        else
            return new Result().error("delete seeker failed maybe cuz it is released by U");
    }

    @PutMapping("/plaza/seeker")
    Result updateSeeker(@RequestBody Seeker seeker) {
        seeker.setUpdateTime(LocalDateTime.now());
        seekerService.updateSeeker(seeker);
        return new Result().success();
    }

    @GetMapping("/plaza/seeker")
    Result selectSeeker(@RequestParam("label") List<String> labels
                        , @RequestParam(defaultValue = "1") Integer page
                        , @RequestParam(defaultValue = "10") Integer size) {
        return new Result().success(seekerService.selectSeekers(labels, page, size));
    }

    @PostMapping("/plaza/seeker/invite/{seekerID}/{teamID}")
    Result inviteSeeker(@RequestBody InviteRequest inviteRequest
            , @PathVariable("seekerID") Integer seekerID
            , @PathVariable("teamID") Integer teamID
            , @RequestParam("files") List<MultipartFile> files)  {

        Map<String, Object> userClaims = ThreadLocalUtil.get();
        inviteRequest.setReleaserID((int)userClaims.get("userID"));
        inviteRequest.setTeamID(teamID);

        Integer inviteID = seekerService.insertInviteRequest(inviteRequest, seekerID);
        inviteRequest.setFiles(FileConverter.convertToPojoFileList(files, inviteID));
        seekerService.insertInviteFiles(inviteRequest);

        return new Result().success();
    }

    @GetMapping("/user/{userID}/seekers")
    Result getSeekerByUserID(@PathVariable("userID") Integer userID) {
        return new Result().success(seekerService.getSeekersByUserId(userID));
    }
}
