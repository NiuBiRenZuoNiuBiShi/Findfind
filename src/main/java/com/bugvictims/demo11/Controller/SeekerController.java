package com.bugvictims.demo11.Controller;

import com.bugvictims.demo11.Pojo.InviteRequest;
import com.bugvictims.demo11.Pojo.Result;
import com.bugvictims.demo11.Pojo.Seeker;
import com.bugvictims.demo11.Service.SeekerService;
import com.bugvictims.demo11.Service.impl.SeekerServiceImpl;
import com.bugvictims.demo11.Utils.FileConverter;
import com.bugvictims.demo11.Utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@RestController
public class SeekerController {

    @Autowired
    SeekerService seekerService;
    @Autowired
    private SeekerServiceImpl seekerServiceImpl;

    @PostMapping("/plaza/seeker")
    Result createSeeker(@ModelAttribute Seeker seeker, @RequestParam(value = "labels", required = true) List<String> labels, @RequestParam(value = "files", required = false) List<MultipartFile> files, @RequestParam(value = "fileNames", required = false) List<String> fileNames) {
        Map<String, Object> userClaims = ThreadLocalUtil.get();
        if (userClaims == null) {
            return new Result().error("当前无用户登录");
        }
        seeker.setSeekerId((Integer) userClaims.get("id"));
        if (labels != null) {
            seeker.setLabels(labels);
        }
        seeker.setCreateTime(LocalDateTime.now());
        seeker.setUpdateTime(LocalDateTime.now());
        Integer seekerID = seekerService.insertSeeker(seeker);

        if (files != null && !files.isEmpty()) {
            seeker.setSeekerFiles(FileConverter.convertToPojoFileList(files, seekerID));
            for (int i = 0; i < fileNames.size(); i++) {
                seeker.getSeekerFiles().get(i).setFileName(fileNames.get(i));
                //System.out.println(files.get(i).getSize());
            }
            seekerService.insertSeekerFiles(seeker);
        }
        return new Result().success();
    }

    @DeleteMapping("/plaza/seeker/{id}")
    Result deleteSeeker(@PathVariable Integer id) {
        if (seekerService.deleteSeeker(id)) return new Result().success();
        else return new Result().error("delete seeker failed maybe cuz it is released by U");
    }

    @PutMapping("/plaza/seeker")
    Result updateSeeker(@ModelAttribute Seeker seeker, @RequestParam(value = "files") List<MultipartFile> files, @RequestParam(value = "fileNames") List<String> fileNames) {
        seeker.setUpdateTime(LocalDateTime.now());
        if (fileNames != null && !fileNames.isEmpty()) {
            IntStream.range(0, fileNames.size()).forEach(i -> seeker.getSeekerFiles().get(i).setFileName(fileNames.get(i)));
        }
        seekerService.updateSeeker(seeker);
        return new Result().success();
    }

    @GetMapping("/plaza/seeker")
    Result selectSeeker(@RequestParam("labels") List<String> labels, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return new Result().success(seekerService.selectSeekers(labels, page, size));
    }

    @PostMapping("/plaza/seeker/invite/{seekerID}/{teamID}")
    Result inviteSeeker(@ModelAttribute InviteRequest inviteRequest, @PathVariable("seekerID") Integer seekerID, @PathVariable("teamID") Integer teamID, @RequestParam("files") List<MultipartFile> files, @RequestParam("fileNames") List<String> fileNames) {

        Map<String, Object> userClaims = ThreadLocalUtil.get();
        inviteRequest.setReleaserID((int) userClaims.get("id"));
        inviteRequest.setTeamID(teamID);

        if (seekerService.insertInviteRequest(inviteRequest, seekerID) == -1) {
            return new Result().error("重复邀请");
        }

        if (fileNames != null && !fileNames.isEmpty()) {
            inviteRequest.setInviteFiles(FileConverter.convertToPojoFileList(files, inviteRequest.getId()));
            IntStream.range(0, inviteRequest.getInviteFiles().size()).forEach(i -> inviteRequest.getInviteFiles().get(i).setFileName(fileNames.get(i)));
            seekerService.insertInviteFiles(inviteRequest);
        }

        return new Result().success();
    }

    @GetMapping("/user/{userID}/seekers")
    Result getSeekerByUserID(@PathVariable("userID") Integer userID) {
        return new Result().success(seekerService.getSeekersByUserId(userID));
    }

    @GetMapping("/plaza/seeker/files")
    Result getSeekerFilesBySeekerID(@RequestParam Integer seekerId) {
        return new Result().success(seekerService.selectSeekerFilesBySeekerId(seekerId));
    }
}
