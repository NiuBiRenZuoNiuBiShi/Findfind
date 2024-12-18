package com.bugvictims.demo11.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Label {
    String label;
    Integer seekerId;
    Integer id;
    LocalDateTime createTime;
    LocalDateTime updateTime;
}
