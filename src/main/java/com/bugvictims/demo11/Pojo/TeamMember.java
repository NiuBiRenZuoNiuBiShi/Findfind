package com.bugvictims.demo11.Pojo;

public class TeamMember {
    private String id;
    private String isCaptain;
    private String isLeader;
    private String joinDate;
    private String role;
    private User user;

    public String getid() { return id; }
    public void setid(String value) { this.id = value; }

    public String getIsCaptain() { return isCaptain; }
    public void setIsCaptain(String value) { this.isCaptain = value; }

    public String getIsLeader() { return isLeader; }
    public void setIsLeader(String value) { this.isLeader = value; }

    public String getJoinDate() { return joinDate; }
    public void setJoinDate(String value) { this.joinDate = value; }

    public String getRole() { return role; }
    public void setRole(String value) { this.role = value; }

    public User getUser() { return user; }
    public void setUser(User value) { this.user = value; }
}