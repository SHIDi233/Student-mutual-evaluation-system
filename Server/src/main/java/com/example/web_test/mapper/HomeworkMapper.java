package com.example.web_test.mapper;

import com.example.web_test.pojo.Appeal;
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

    @Update("update homework set isPublish=true, ddl=#{ddl}, content=#{content}, hwName=#{name}, score=#{score}, cID=#{cID} where hwID=#{hwID}")
    void publish(int hwID, LocalDateTime ddl, String content, String name, int score, int cID);

    @Insert("insert into homework_member(hwID, uID, cID) values(#{hwID}, #{uid}, #{cID})")
    void addHomework(int uid, int hwID, int cID);

    List<HomeworkMember> getMyHomework(int uID, Integer cID);

    @Select("select * from homework_member where uID=#{uID} and hwID=#{hwID}")
    HomeworkMember getHomework_stu(int uID, int hwID);

    @Update("update homework_member set content=#{content} where uID=#{uID} and hwID=#{hwID}")
    void saveHomework_stu(int uID, int hwID, String content);

    @Update("update homework_member set isSubmit=true, content=#{content} where uID=#{uID} and hwID=#{hwID}")
    void submitHomework(int uID, int hwID, String content);

    @Select("select * from homework_member where hwID=#{hwID}")
    List<HomeworkMember> getStudentHomeworks(int hwID);

    @Select("select count(*) from homework_member where hwID=#{hwID}")
    Integer getHomeworkMemberNum(int hwID);

    @Select("select count(*) from homework_member where hwID=#{hwID} and isSubmit=true")
    Integer getSubmitNum(int hwID);

    @Select("select * from homework_member where hwID=#{hwID} and isSubmit=true")
    List<HomeworkMember> getSubmitHomeworks(int hwID);

    @Select("select * from homework_member where hwID=#{hwID} and isSubmit=false")
    List<HomeworkMember> getAbsent(int hwID);

//    @Update("update homework_member set score=#{score}, comment=#{comment} where uID=#{sID} and hwID=#{hwID}")
@Update("update evaluationstat set score=#{score} where uID=#{sID} and hwID=#{hwID}")
    void mark(int sID, int hwID, int score, String comment);

    @Update("update homework set ddl=now() where hwID=#{hwID}")
    void ddl(int hwID);

    @Insert("insert into appeal(cID, hwID, uID, content) values(#{cID}, #{hwID}, #{uID}, #{content})")
    void appeal(int uID, int hwID, int cID, String content);

    @Select("select * from appeal where hwID=#{hwID}")
    List<Appeal> getAppeal(int hwID);

    @Select("select * from appeal where uID=#{uID} and hwID=#{hwID}")
    List<Appeal> getMyAppeal(int uID, int hwID);

    @Update("update appeal set result=#{result}, isHandled=true where hwID=#{hwID} and uID=#{sID}")
    void handleAppeal(int hwID, int sID, String result);

    @Select("select * from appeal where hwID=#{hwID} and uID=#{sID}")
    Appeal getAppealByID(int hwID, int sID);
}
