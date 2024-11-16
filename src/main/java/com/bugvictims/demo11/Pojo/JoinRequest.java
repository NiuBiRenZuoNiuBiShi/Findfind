package com.bugvictims.demo11.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequest {
    private int id;
    private int userId;
    private int teamId;
    private String message;
    private String response;
    private int status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
