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
    private Integer seekerId; // 本来是releaserID，但是数据库里是seekerId，遂改
    private String header;
    private List<String> labels;
    private String message;
    private String position;
    private LocalDateTime createTime;
    private List<PojoFile> seekerFiles;
    private LocalDateTime updateTime;
}
