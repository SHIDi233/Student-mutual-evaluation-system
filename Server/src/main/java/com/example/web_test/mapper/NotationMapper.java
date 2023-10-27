package com.example.web_test.mapper;

import com.example.web_test.pojo.Notation;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotationMapper {

    @Select("select * from notation")
    public List<Notation> select();

    @Insert("insert into notation(senderName, scope, content, receiverID) VALUES (#{senderName}, #{scope}, #{content}, #{receiverID})")
    public void createNote(String senderName, String scope, String content, int receiverID);

    @Select("select * from notation where scope=#{wName}")
    public List<Notation> getWareNote(String wName);

    @Select("select * from notation where receiverID=#{uID}")
    public List<Notation> getNote(int uID);

    @Select("select * from notation where ID=#{nID}")
    public Notation getNotation(int nID);

    @Insert("insert into notation(senderName, scope, content, receiverID, type, approveID) VALUES (#{senderName}, #{scope}, #{content}, #{receiverID}, #{type}, #{approveID})")
    void createNote1(Notation notation);

    @Update("update notation set type=#{newType} where ID=#{aID}")
    void updateNotation(int aID, String type, String newType);
}
