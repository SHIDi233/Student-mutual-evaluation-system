package com.example.web_test.mapper;

import com.example.web_test.pojo.ClassApproval;
import com.example.web_test.pojo.ClassMember;
import com.example.web_test.pojo.Classes;
import com.example.web_test.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ClassMapper {

    @Select("select * from class where classID in(select classID from class_member where uID=#{ID})")
    public List<Classes> list(int ID);

    @Options(keyProperty = "classID", useGeneratedKeys = true)
    @Insert("insert into class(adminID, className, subject, introduction, code) values(#{adminID}, #{className}, #{subject}, #{introduction}, #{code})")
    void createClass(Classes c);

    @Insert("insert into class_member(classID, uID, role) VALUES (#{cID}, #{uID}, #{role})")
    void addMember(int cID, int uID, int role);

    @Select("select * from class where classID=#{cID}")
    Classes getClasses(int cID);

    @Update("update class set code=#{code} where classID=#{ID}")
    void setCode(String code, int ID);

    @Select("select * from class where code=#{code}")
    Classes getByCode(String code);

    @Options(keyProperty = "caID", useGeneratedKeys = true)
    @Insert("insert into classapproval(senderID, type, content, classID) VALUES (#{senderID}, #{type}, #{content}, #{classID})")
    void createApproval(ClassApproval classApproval);

    @Select("select uID from class_member where classID=#{cID} and (role=3 or role=4)")
    List<Integer> getAdmin(int cID);

    @Update("update classapproval set `grant`=#{grant} where caID=#{caID}")
    void updateApproval(int caID, int grant);

    @Select("select * from classapproval where caID=#{caID}")
    ClassApproval getClassApproval(int caID);

    @Update("update class set numOfMember=numOfMember+1 where classID=#{classID}")
    void updateClassNum(int classID);

    @Delete("delete from class where classID=#{cID}")
    void deleteClass(int cID);

    @Delete("delete from class_member where classID=#{cID}")
    void deleteMembers(int cID);

    @Delete("delete from classapproval where classID=#{cID}")
    void deleteApproval(int cID);

    @Select("select * from user where ID in (select uID from class_member where classID=#{cID} order by class_member.role)")
    List<User> getMembers(int cID);

    @Select("select role from class_member where classID=#{cID} and uID=#{uID}")
    Integer getRole(int uID, int cID);

    @Delete("delete from class_member where classID=#{cID} and uID=#{kID}")
    void kickMember(int cID, int kID);

    @Select("select * from class_member where classID=#{cID}")
    List<ClassMember> listMembers(int cID);

    @Select("select count(*) from class_member where classID=#{classID}")
    int getNumOfStu(int classID);
}
