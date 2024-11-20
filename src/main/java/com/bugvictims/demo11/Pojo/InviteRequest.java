package com.bugvictims.demo11.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InviteRequest {

    private Integer id;
    private Integer userID;
    private Integer teamID;
    private Integer releaserID;
    private Integer header;
    private String message;
    private String response;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}