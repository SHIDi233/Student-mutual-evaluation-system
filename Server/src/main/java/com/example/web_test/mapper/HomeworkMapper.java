package com.example.web_test.mapper;

import com.example.web_test.pojo.ClassMember;
import com.example.web_test.pojo.Homework;
import com.example.web_test.pojo.HomeworkMember;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface HomeworkMapper {

    @Options(keyProperty = "hwID", useGeneratedKeys = true)
    @Insert("insert into homework(creatorID) values(#{creatorID})")
    public void createHomework(Homework homework);

    @Select("select * from homework where hwID=#{hwID}")
    public Homework getHomework(int hwID);

    @Update("update homework set hwName=#{name}, content=#{content} where hwID=#{hwID}")
    void saveHomework(int hwID, String name, String content);

    @Select("select * from homework where creatorID=#{uID}")
    List<Homework> listHomework(int uID);

    @Update("update homework set isPublish=true, ddl=#{ddl}, content=#{content}, hwName=#{name} where hwID=#{hwID}")
    void publish(int hwID, LocalDateTime ddl, String content, String name);

    @Insert("insert into homework_member(hwID, uID, cID) values(#{hwID}, #{uid}, #{cID})")
    void addHomework(int uid, int hwID, int cID);


    List<HomeworkMember> getMyHomework(int uID, Integer cID);

    @Select("select * from homework_member where uID=#{uID} and hwID=#{hwID}")
    HomeworkMember getHomework_stu(int uID, int hwID);

    @Update("update homework_member set content=#{content} where uID=#{uID} and hwID=#{hwID}")
    void saveHomework_stu(int uID, int hwID, String content);

    @Update("update homework_member set isSubmit=true, content=#{content} where uID=#{uID} and hwID=#{hwID}")
    void submitHomework(int uID, int hwID, String content);
}
