package com.example.web_test.mapper;

import com.example.web_test.pojo.User;
import com.example.web_test.pojo.Warehouse;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WareMapper {

    @Select("select * from warehouse where wID = #{wID}")
    public List<Warehouse> list(int wID);

    @Select("select * from warehouse where wName=#{wName}")
    public List<Warehouse> list_name(String wName);

    @Select("select * from warehouse where adminID = #{adminID} and wName = #{wName}")
    public List<Warehouse> isAdmin(String wName, int adminID);

    @Options(keyProperty = "wID", useGeneratedKeys = true)
    @Insert("insert into warehouse(wName, adminID, cTime, uTime, wPath) " +
            "values (#{wName}, #{adminID}, #{cTime}, #{uTime}, #{wPath})")
    public void insertWare(Warehouse ware);

    @Select("select wID from warehouse where wName=#{wName}")
    public Integer getWareID(String wName);
}
