package com.example.web_test.server.impl;

import com.example.web_test.mapper.UserMapper;
import com.example.web_test.pojo.Certification;
import com.example.web_test.pojo.Result;
import com.example.web_test.pojo.User;
import com.example.web_test.server.LoginServer;
import com.example.web_test.utils.EncodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class LoginServerA implements LoginServer {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String mail, String password) {
        String cpw = EncodeUtils.bCryptEncode(password);
        List<User> users = userMapper.login(mail);
        for (User user : users) {
            if(EncodeUtils.bCryptMatch(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public int register(String mail, String password, String name) {
        //判断邮箱是否被注册
        List<User> users = userMapper.getUsers(mail);
        if(users.size() != 0) {
            return -1;
        }

        User user = new User();
        user.setMail(mail);
        String cpw = EncodeUtils.bCryptEncode(password);
        user.setPassword(cpw);
        user.setName(name);

        userMapper.register(user);

        return user.getID();
    }

    /**
     *
     * @param ID 用户ID
     * @param school 学校
     * @param majority 专业
     * @param introduction 个人简介
     * @param studentID 学号
     * @return -1：认证失败
     *         -2：信息不完整
     */
    @Override
    public int perfect(int ID, String school, String majority, String introduction, String studentID) {
        //判断是否有缺失的信息
        if(school == null || studentID == null) {
            return -2;
        }

        //查找数据库中是否有认证信息
        List<Certification> certification = userMapper.certificate(school, studentID);
        if (certification.size() == 0 || certification.get(0).getUID() != null) {
            if(certification.get(0).getUID() != ID) {
                return -1;
            }
        }

        //认证
        userMapper.certification(school, studentID, ID);
        userMapper.modifyInfo(ID, school, majority, introduction, studentID, certification.get(0).getRole());
        return 0;
    }

    //获取头像url
    @Override
    public String getHeader(int uID) {
        return userMapper.getHeader(uID);
    }

    /**
     *
     * @param uID 用户ID
     * @return name：用户名
     *         introduction：个人简介
     *         readme：个人主页readme
     *         school：学校
     *         majority：专业
     *         studentID：学号
     */
    @Override
    public Map<String, String> getInfo(int uID) {
        User user = userMapper.getUser(uID);
        Map<String, String> info = new HashMap<>();
        info.put("name", user.getName());
        info.put("introduction", user.getIntroduction());
        info.put("readme", user.getReadme());
        info.put("school", user.getSchool());
        info.put("majority", user.getMajority());
        info.put("studentID", user.getStudentID());
        return info;
    }

    @Override
    public boolean isTeacher(int uID) {
        User user = userMapper.getUser(uID);
        return user.getRole() == 1;
    }


    @PostMapping("/uploadHeader")
    public Result uploadHeader(MultipartFile image) throws IOException {
        String originalFileName = image.getOriginalFilename();
        String newFileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));
        image.transferTo(new File("D:/vue/image/" + newFileName));
        return Result.success();
    }

    @Override
    public int modifyReadme(int uID, String readme) {
        userMapper.modifyReadme(uID, readme);
        return 0;
    }

    @Override
    public void setHeader(int uID, String filePath) {
        userMapper.setHeader(uID, filePath);
    }

    @Override
    public int getRole(int uID) {
        return userMapper.getUser(uID).getRole();
    }
}
