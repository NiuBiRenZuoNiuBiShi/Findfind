package com.bugvictims.demo11.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recruit {
    private Integer id;
    private Integer teamID;
    private Integer releaserID;
    private Integer needNum;
    private Integer hasNum;
    private Integer receiveNum;
    private String header;
    private String message;
    private List<PojoFile> recruitFiles;
    private List<String> labels;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
