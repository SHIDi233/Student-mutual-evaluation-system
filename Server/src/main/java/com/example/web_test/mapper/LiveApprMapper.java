package com.example.web_test.mapper;

import com.example.web_test.pojo.LiveApproval;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface LiveApprMapper {

    @Insert("insert into liveApproval(senderID, receiverID, wID, content, startTime) " +
            "values (#{sID}, #{rID}, #{wID}, #{content}, str_to_date(#{sTime}, 'YYYY-mm-dd'))")
    public void initiate(int sID, int rID, int wID, String content, String sTime);

    @Select("select * from liveApproval")
    public List<LiveApproval> select();

    @Update("update liveApproval set `grant`=#{grant} where laID=#{laID}")
    public void appr(int laID, int grant);

    @Select("select * from liveApproval where laID=#{laID}")
    public LiveApproval getByLaID(int laID);

}
