package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.InviteRequest;
import com.bugvictims.demo11.Pojo.PojoFile;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface InviteRequestFileMapper {

    void insertInviteFile(InviteRequest inviteRequest);

    @Select("SELECT (id, file, invite_id, type, create_time, update_time)" +
            "FROM invite_request_file" +
            " WHERE invite_id = #{id}")
    List<PojoFile> selectInviteFiles(InviteRequest inviteRequest);

    @Delete("DELETE FROM invite_request_file WHERE invite_id = #{id}")
    void deleteInviteFile(InviteRequest inviteRequest);
}