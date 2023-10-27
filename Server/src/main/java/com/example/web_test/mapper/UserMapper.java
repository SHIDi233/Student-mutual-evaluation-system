package com.example.web_test.mapper;

import com.example.web_test.pojo.Certification;
import com.example.web_test.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from User where mail = #{mail} and password = #{password}")
    public List<User> login(String mail, String password);

    @Select("select * from User where mail = #{mail}")
    public List<User> getUsers(String mail);

    @Options(keyProperty = "ID", useGeneratedKeys = true)
    @Insert("insert into User(Name, mail, password) values (#{name}, #{mail}, #{password})")
    public void register(User user);

    @Select("select * from User where ID=#{uID}")
    public User getUser(int uID);

    @Update("update User set school=#{school}, majority=#{majority}, introduction=#{introduction}, studentID=#{studentID}, role=#{role} where ID=#{ID}")
    public void modifyInfo(int ID, String school, String majority, String introduction, String studentID, int role);

    @Select("select head from user where ID=#{ID}")
    public String getHeader(int ID);

    @Select("select * from certification where school=#{school} and studentID=#{studentID}")
    List<Certification> certificate(String school, String studentID);

    @Update("update certification set uID=#{id} where school=#{school} and studentID=#{studentID}")
    void certification(String school, String studentID, int id);

    @Update("update user set readme=#{readme} where ID=#{uID}")
    void modifyReadme(int uID, String readme);


    @Update("update user set head=#{filePath} where ID=#{uID}")
    void setHeader(int uID, String filePath);
}
