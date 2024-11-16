package com.bugvictims.demo11.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor//生成无参构造方法
@AllArgsConstructor//生成全参构造方法
@Data//生成get set方法
public class Team {
    private String description;//队伍描述
    private Integer id;//主键ID
    private String[] label;//队伍标签
    private String name;//队伍名称
    private String position;//队伍地理位置
    private Integer status;//队伍状态  1可被搜索到  2不可被搜索到 3已解散
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间
}