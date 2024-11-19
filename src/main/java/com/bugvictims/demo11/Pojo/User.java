package com.bugvictims.demo11.Pojo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
        private int id;
        private String username;
        @JsonIgnore//转换成json时忽略密码
        private String password;
        private String nickName;
        private String email;
        private String phone;
        private String biology;
        private String postion;
        private int status=2;//1为能查看，0不能，2初始化
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
}