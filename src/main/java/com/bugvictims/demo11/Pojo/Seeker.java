package com.bugvictims.demo11.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seeker {
    private int id;
    private String seekerID;
    private String Header;
    private String[] label;
    private String message;
    private String position;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
