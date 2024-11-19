package com.bugvictims.demo11.Pojo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor

public class User {
        @NotNull
        private int id;
        @Pattern(regexp="^.{4,16}$")
        private String username;
        //@JsonIgnore//转换成json时忽略密码
        @Pattern(regexp="^.{4,16}$")
        private String password;
        @Pattern(regexp="^.{11}$")
        private String phone;
        @Pattern(regexp="^.{1,40}$")
        private String biology;


        @Min(0)
        @Max(1)
        private int status;//1为能查看，0不能

        @Email
        private String email;

        @Pattern(regexp="^.{0,20}$")
        private String position;

        @Pattern(regexp="^.{1,10}$")
        private String nickName;

        private LocalDateTime createTime;
        private LocalDateTime updateTime;
}