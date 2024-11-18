package com.bugvictims.demo11.Service;

import com.bugvictims.demo11.Pojo.InviteRequest;
import com.bugvictims.demo11.Pojo.Seeker;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SeekerService {
    void createSeeker(Seeker seeker);

    void deleteSeeker(Integer id);

    void updateSeeker(Seeker seeker);

    List<Seeker> getSeekers(List<String> labels);

    void inviteSeeker(InviteRequest inviteRequest, Integer seekerID);
}
