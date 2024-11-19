package com.bugvictims.demo11.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeamUser {
    private String id;//主键ID
    private int teamId;//所属队伍ID
    private Integer userId;//用户ID
    private String type;//用户身份
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间
}