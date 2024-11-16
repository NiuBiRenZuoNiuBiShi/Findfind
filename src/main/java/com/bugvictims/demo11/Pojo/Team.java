package com.bugvictims.demo11.Pojo;

public class Team {
    private String description;
    private String id;
    private String[] label;
    private TeamMember[] members;
    private String name;
    private String postion;
    private String status;

    public String getDescription() { return description; }
    public void setDescription(String value) { this.description = value; }

    public String getid() { return id; }
    public void setid(String value) { this.id = value; }

    public String[] getLabel() { return label; }
    public void setLabel(String[] value) { this.label = value; }

    public TeamMember[] getMembers() { return members; }
    public void setMembers(TeamMember[] value) { this.members = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public String getPostion() { return postion; }
    public void setPostion(String value) { this.postion = value; }

    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value; }
}