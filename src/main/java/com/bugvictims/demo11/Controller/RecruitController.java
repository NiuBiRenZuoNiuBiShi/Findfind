package com.bugvictims.demo11.Controller;

import com.bugvictims.demo11.Pojo.JoinRequest;
import com.bugvictims.demo11.Pojo.Recruit;
import com.bugvictims.demo11.Pojo.Result;
import com.bugvictims.demo11.Service.JoinRequestService;
import com.bugvictims.demo11.Service.RecruitService;
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
public class RecruitController {

    @Autowired
    private RecruitService recruitService;

    @Autowired
    private JoinRequestService joinRequestService;

    /*
        由于recruit的PojoFile需要一个linkID,才能插入文件到数据库中，否则两个数据库没有关联
        因此先流程先把recruit插入到相应的数据库中,返回recruit在数据库的id
        在插入文件
    */
    @PostMapping("/recruit/release/{teamID}")
    public Result releaseRecruit(@ModelAttribute Recruit recruit, @RequestParam(value = "labels", required = true) List<String> labels, @RequestParam(value = "files", required = false) List<MultipartFile> files, @RequestParam(value = "fileNames", required = false) List<String> fileNames) {
        //System.out.println(recruit.getNeedNum());
        Map<String, Object> userClaims = ThreadLocalUtil.get();
        recruit.setReleaserID((int) userClaims.get("id"));
        recruit.setReceiveNum(0);
        recruit.setHasNum(0);

        if (labels != null) {
            recruit.setLabels(labels);
        } else {
            return new Result().error("无标签");
        }

        recruitService.insertRecruit(recruit); // 得到插入的ID

        if (files != null && !files.isEmpty()) {
            recruit.setRecruitFiles(FileConverter.convertToPojoFileList(files, recruit.getId()));
            IntStream.range(0, recruit.getRecruitFiles().size()).forEach(i -> recruit.getRecruitFiles().get(i).setFileName(fileNames.get(i)));
            recruitService.insertRecruitFiles(recruit);
        }


        return new Result().success();
    }

    @PutMapping("/plaza")
    public Result updateRecruit(@ModelAttribute Recruit recruit, @RequestParam(value = "files", required = false) List<MultipartFile> files, @RequestParam(value = "fileNames", required = false) List<String> fileNames) {
        recruit.setUpdateTime(LocalDateTime.now());
        if (fileNames != null && !fileNames.isEmpty()) {
            recruit.setRecruitFiles(FileConverter.convertToPojoFileList(files, recruit.getId()));
            IntStream.range(0, recruit.getRecruitFiles().size()).forEach(i -> recruit.getRecruitFiles().get(i).setFileName(fileNames.get(i)));
        }
        recruitService.updateRecruit(recruit);

        return new Result().success();
    }

    @GetMapping("/plaza") // 通过label来筛选，所需要的Recruit，分页查询
    public Result getRecruits(@RequestParam List<String> labels, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return new Result().success(recruitService.selectRecruits(labels, page, size));
    }

    @DeleteMapping("/plaza/{id}")
    public Result deleteRecruit(@PathVariable Integer id) {
        if (id < 0) {
            return new Result().error("id not legal");
        }
        if (recruitService.deleteRecruit(id)) return new Result().success();
        else {
            return new Result().error("delete failed because the recruit is not published by U");
        }
    }

    @PostMapping("/plaza/{recruitID}") // 传入recruitID,在Body里传入具体的内容
    public Result addJoinRequest(@PathVariable Integer recruitID, @ModelAttribute JoinRequest joinRequest, @RequestParam("files") List<MultipartFile> files, @RequestParam("fileNames") List<String> fileNames) {
        Map<String, Object> userClaims = ThreadLocalUtil.get();
        joinRequest.setUserId((int) userClaims.get("id"));
        if (recruitService.insertJoinRequest(recruitID, joinRequest) == -1) {
            return new Result().error("已经申请过了");
        }

        if (fileNames != null && !fileNames.isEmpty()) {
            joinRequest.setJoinFiles(FileConverter.convertToPojoFileList(files, joinRequest.getId()));
            IntStream.range(0, joinRequest.getJoinFiles().size()).forEach(i -> joinRequest.getJoinFiles().get(i).setFileName(fileNames.get(i)));
            recruitService.insertJoinFiles(joinRequest);
        }
        return new Result().success();
    }

    @GetMapping("/plaza/recruit/files")
    public Result getRecruitFiles(@RequestParam Integer recruitId) {
        return new Result().success(recruitService.selectRecruitFilesByItsId(recruitId));
    }


}
