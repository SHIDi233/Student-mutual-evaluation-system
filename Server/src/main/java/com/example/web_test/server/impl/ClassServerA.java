package com.example.web_test.server.impl;

import com.example.web_test.mapper.ClassMapper;
import com.example.web_test.mapper.NotationMapper;
import com.example.web_test.mapper.UserMapper;
import com.example.web_test.pojo.*;
import com.example.web_test.server.ClassServer;
import com.example.web_test.utils.ExcelData;
import com.example.web_test.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ClassServerA implements ClassServer {

    @Autowired
    ClassMapper classMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    NotationMapper notationMapper;

    @Override
    public List<Map<String, Object>> getClasses(int ID) {
        List<Classes> classes = classMapper.list(ID);
        List<Map<String, Object>> res = new ArrayList<>();
        for(Classes c : classes) {
            int role = classMapper.getRole(ID, c.getClassID());
            Map<String, Object> m = new HashMap<>();
            m.put("classID", c.getClassID());
            m.put("className", c.getClassName());
            m.put("introduction", c.getIntroduction());
            m.put("role", role);
            res.add(m);
        }
        return res;
    }

    @Override
    public int createClass(int uID, String name, String subject,String introduction) {
        //判读权限
        if(userMapper.getUser(uID).getRole() != 1) { return 1; }
        //生成班级码
        String code = JwtUtils.getUuid16();
        //数据库中创建班级
        Classes c = new Classes();
        c.setAdminID(uID);
        c.setClassName(name);
        c.setIntroduction(introduction);
        c.setSubject(subject);
        c.setCode(code);
        classMapper.createClass(c);
        classMapper.addMember(c.getClassID(), uID, 4);
        return 0;
    }

    @Override
    public int joinClass(int uID, int cID) {
        //获取用户信息
        User user = userMapper.getUser(uID);
        if(user == null) { return -1; }
        //获取班级
        Classes c = classMapper.getClasses(cID);
        if(c == null) { return 1; }
        //查看是否已经在班级中
        if(classMapper.getRole(uID, cID) != null) { return -2; }
        //创建审批
        ClassApproval classApproval = new ClassApproval();
        classApproval.setSenderID(uID);
        classApproval.setClassID(cID);
        classApproval.setContent("");
        classApproval.setType(0);
        classMapper.createApproval(classApproval);
        //查询管理员列表
        List<Integer> admins = classMapper.getAdmin(cID);
        //创建公告
        Notation notation = new Notation();
        notation.setContent(String.format("用户：%s 申请加入该班级。\n学号：%s", user.getName(), user.getStudentID()));
        notation.setScope("班级: " + c.getClassName());
        notation.setType("CA");
        notation.setSenderName(user.getName());
        notation.setApproveID(classApproval.getCaID());
        for(int i : admins) {
            notation.setReceiverID(i);
            notationMapper.createNote1(notation);
        }
        return 0;
    }

    @Override
    public List<Map<String, Object>> listMembers(int uID, int cID) {
        List<User> users = classMapper.getMembers(cID);
        List<Map<String, Object>> res = new ArrayList<>();
        for(User user : users) {
//            if(uID == user.getID()) { continue; }
            Map<String, Object> m = new HashMap<>();
            m.put("header", user.getHead());
            m.put("name", user.getName());
            m.put("studentID", user.getStudentID());
            m.put("uID", user.getID());
            m.put("role", user.getRole());
            res.add(m);
        }
        return res;
    }

    @Override
    public int kickMember(int uID, int cID, int kID) {
        //判断权限
        int role = classMapper.getRole(uID, cID);
        if(role < 3) {
            return -1;
        }
        //踢出成员
        classMapper.kickMember(cID, kID);
        return 0;
    }

    @Override
    public Map<String, Object> getCode(int cID) {
        Classes classes = classMapper.getClasses(cID);
        Map<String, Object> res = new HashMap<>();
        res.put("code", classes.getCode());
        res.put("className", classes.getClassName());
        res.put("classID", classes.getClassID());
        return res;
    }

    @Override
    public Map<String, Object> searchClass(String code) {
        Classes c = classMapper.getByCode(code);
        if(c == null) { return null; }
        Map<String, Object> m = new HashMap<>();
        m.put("classID", c.getClassID());
        m.put("className", c.getClassName());
        m.put("subject", c.getSubject());
        m.put("introduction", c.getIntroduction());
        return m;
    }

    @Override
    public int deleteClass(int uID, int cID) {
        //权限判断
        Classes classes = classMapper.getClasses(cID);
        if(classes.getAdminID() != uID) {
            return -1;
        }
        classMapper.deleteMembers(cID);
        classMapper.deleteApproval(cID);
//        classMapper.deleteClass(cID);
        return 0;
    }

    @Override
    public List<String> importMember(int uID, int cID, List<ExcelData> dataList) {
        List<String> res = new ArrayList<>();
        Classes classes = classMapper.getClasses(cID);
        if(classes.getAdminID() != uID) {
            res.add("您无权执行此操作");
            return res;
        }
        for(ExcelData data : dataList) {
            List<Certification> certificate = userMapper.certificate(data.getName(), data.getID());
            if (certificate.size() == 0) { //找不到认证
                res.add(data.getName() + " " + data.getID() + ": 找不到该学生");
                continue;
            }
            if(certificate.get(0).getUID() == null) {
                res.add(data.getName() + " " + data.getID() + ": 该学生仍未注册");
                continue;
            }
            //查看是否已在班级里
            Integer role = classMapper.getRole(certificate.get(0).getUID(), cID);
            if(role != null) {
                res.add(data.getName() + " " + data.getID() + ": 该学生已在班级内");
                continue;
            }
            //加入班级
            classMapper.addMember(cID, certificate.get(0).getUID(), 0);
            //发送通知
            User user = userMapper.getUser(uID);
            notationMapper.createNote(user.getName(), classes.getClassName(), "您已被老师邀请加入班级: " + classes.getClassName(), certificate.get(0).getUID());
            res.add(data.getName() + " " + data.getID() + ": 学生导入班级成功");
        }
        return res;
    }
}
