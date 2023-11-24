package com.example.web_test.server;

import com.example.web_test.pojo.User;

import java.util.List;
import java.util.Map;

public interface LoginServer {

    public User login(String mail, String password);

    public int register(String mail, String password, String name);

    public int perfect(int ID, String school, String majority, String introduction, String studentID);

    String getHeader(int uID);

    Map<String, String> getInfo(int uID);

    boolean isTeacher(int uID);

    int modifyReadme(int uID, String readme);

    void setHeader(int uID, String filePath);

    int getRole(int uID);
}
