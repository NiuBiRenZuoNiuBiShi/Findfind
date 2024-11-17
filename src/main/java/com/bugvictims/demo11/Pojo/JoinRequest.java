package com.bugvictims.demo11.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequest {
    private Integer id;
    private Integer userId;
    private Integer teamId;
    private String message;
    private String response;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
