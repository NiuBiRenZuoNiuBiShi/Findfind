package com.bugvictims.demo11.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToEmail implements Serializable {

    private String tos;
    
    private String subject;

    private String content;
}
