package com.example.web_test.mapper;

import com.example.web_test.pojo.Group;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GroupMapper {

    //返回用户加入的所以仓库
    @Select("select * from `group` where uID = #{uID}")
    public List<Group> getWares(int uID);

    //返回仓库包含的成员
    @Select("select * from `group` where wID = #{wID}")
    public List<Group> getMembers(int wID);

    @Insert("insert into `group`(uID, wID) values (#{uID}, #{wID})")
    public void invite(int uID, int wID);
}
