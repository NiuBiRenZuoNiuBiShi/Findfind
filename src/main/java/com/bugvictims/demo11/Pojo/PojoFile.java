package com.bugvictims.demo11.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PojoFile {
    private Integer id;
    private Integer linkedID; /* 这个文件对应的是哪个Recruit?/Seeker? */
    private byte[] fileData;
    private String fileType;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String fileName;

    @JsonIgnore
    private MultipartFile originalFile;

    public PojoFile(MultipartFile multipartFile, Integer linkedID) {
        this.originalFile = multipartFile;
        this.linkedID = linkedID;
        try {
            this.fileData = multipartFile.getBytes();
            this.fileType = multipartFile.getContentType();
            this.createTime = LocalDateTime.now();
            this.updateTime = LocalDateTime.now();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public long getSize() {
        return originalFile != null ? originalFile.getSize() : 0;
    }

    public String getOriginalFileName() {
        return originalFile != null ? originalFile.getOriginalFilename() : null;
    }
}
