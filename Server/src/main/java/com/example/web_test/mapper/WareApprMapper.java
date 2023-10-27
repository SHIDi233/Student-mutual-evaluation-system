package com.example.web_test.mapper;

import com.example.web_test.pojo.WareApproval;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface WareApprMapper {

    @Insert("insert into wareApproval(senderID, receiverID, wID, type, content, inviterID) " +
            "values (#{sID}, #{rID}, #{wID}, #{type}, #{content}, #{iID})")
    public void initiate(int sID, int rID, int wID, int type, String content, int iID);

    @Select("select * from wareApproval where senderID=#{sID} and receiverID=#{rID} and wID=#{wID}")
    public List<WareApproval> selects(int sID, int rID, int wID);

    @Select("select * from wareApproval where receiverID=#{rID}")
    public List<WareApproval> getAppr_rID(int rID);

    @Update("update wareApproval set `grant`=#{grant}, readStat=true where waID=#{waID}")
    public void appr(int waID, int grant);

    @Select("select * from wareApproval where waID=#{waID}")
    public WareApproval getAppr_waID(int waID);
}