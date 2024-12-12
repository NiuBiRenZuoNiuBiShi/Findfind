package com.bugvictims.demo11.Mapper;

import com.bugvictims.demo11.Pojo.JoinRequest;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface JoinRequestFileMapper {

    void insertJoinFile(JoinRequest joinRequest);

    @Delete("DELETE FROM join_request_file WHERE join_id = #{id}")
    void deleteJoinFile(JoinRequest joinRequest);

    @Select("SELECT (id, join_id, file, type, create_time, update_time)" +
            " FROM join_request_file" +
            " WHERE join_id = #{id}")
    JoinRequest selectJoinFile(Integer id);
}
