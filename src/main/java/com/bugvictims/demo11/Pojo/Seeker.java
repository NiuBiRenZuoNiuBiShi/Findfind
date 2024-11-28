package com.bugvictims.demo11.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seeker {
    private Integer id;
    private Integer seekerID;
    private String Header;
    private List<String> label;
    private String message;
    private String position;
    private LocalDateTime createTime;
    private List<PojoFile> files;
    private LocalDateTime updateTime;
}
