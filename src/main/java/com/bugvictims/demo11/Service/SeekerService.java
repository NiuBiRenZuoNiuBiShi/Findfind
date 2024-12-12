package com.bugvictims.demo11.Service;

import com.bugvictims.demo11.Pojo.InviteRequest;
import com.bugvictims.demo11.Pojo.Seeker;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SeekerService {
    Integer insertSeeker(Seeker seeker);

    boolean deleteSeeker(Integer id);

    void updateSeeker(Seeker seeker);

    PageInfo<Seeker> selectSeekers(List<String> labels, Integer page, Integer pageSize);

    Integer insertInviteRequest(InviteRequest inviteRequest, Integer seekerID);

    void insertSeekerFiles(Seeker seeker);

    void insertInviteFiles(InviteRequest inviteRequest);

    List<Seeker> getSeekersByUserId(Integer userID);
}
