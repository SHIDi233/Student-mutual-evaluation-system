package com.example.web_test.mapper;

import com.example.web_test.pojo.SysApproval;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SysApprMapper {

    @Insert("insert into sysApproval(senderID, wName, type, content) VALUES (#{sID}, #{wName}, #{type}, #{content})")
    public void sysAppr(int sID, String wName, String content, int type);

    @Select("select * from sysApproval")
    public List<SysApproval> selects();

    //update wareApproval set `grant`=#{grant}, readStat=true where waID=#{waID}
    @Update("update sysApproval set `grant`=#{grant}, readStat=true where saID=#{saID}")
    public void appr(int saID, int grant);

    @Select("select * from sysApproval where saID=#{saID}")
    public SysApproval getSysAppr(int saID);
}
