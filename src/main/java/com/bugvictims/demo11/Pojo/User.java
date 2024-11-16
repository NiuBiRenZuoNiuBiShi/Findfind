package com.bugvictims.demo11.Pojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
        private int id;
        private String username;
        private String password;
        private String nickName;
        private String email;
        private String phone;
        private String biology;
        private String position;
        private int status=2;//1为能查看，0不能，2初始化
        private Date createTime;
        private Date updateTime;
}