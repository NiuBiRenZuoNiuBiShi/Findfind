package com.bugvictims.demo11.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequest {
    private Integer id;
    private Integer userId;
    private Integer teamId;
    private String message;
    private String response;
    private Integer status;//0:未处理 1:已同意 2:已拒绝
    private List<PojoFile> files;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
