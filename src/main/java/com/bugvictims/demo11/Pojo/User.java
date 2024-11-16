package com.bugvictims.demo11.Pojo;
public class User {
    private String username;
    private String nickname;
    private String password;
    private String phone;
    private String email;
    private String id;
    private String biology;
    private String postion;
    private String[] skillHobby;


    public String getBiology() { return biology; }
    public void setBiology(String value) { this.biology = value; }

    public String getEmail() { return email; }
    public void setEmail(String value) { this.email = value; }

    public String getid() { return id; }
    public void setid(String value) { this.id = value; }

    public String getNickname() { return nickname; }
    public void setNickname(String value) { this.nickname = value; }

    public String getPassword() { return password; }
    public void setPassword(String value) { this.password = value; }

    public String getPhone() { return phone; }
    public void setPhone(String value) { this.phone = value; }

    public String getPostion() { return postion; }
    public void setPostion(String value) { this.postion = value; }

    public String[] getSkillHobby() { return skillHobby; }
    public void setSkillHobby(String[] value) { this.skillHobby = value; }

    public String getUsername() { return username; }
    public void setUsername(String value) { this.username = value; }
}