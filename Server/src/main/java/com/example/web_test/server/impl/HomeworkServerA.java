package com.example.web_test.server.impl;

import com.example.web_test.mapper.*;
import com.example.web_test.pojo.*;
import com.example.web_test.server.HomeworkServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HomeworkServerA implements HomeworkServer {

    @Autowired
    UserMapper userMapper;

    @Autowired
    HomeworkMapper homeworkMapper;

    @Autowired
    ClassMapper classMapper;

    @Autowired
    NotationMapper notationMapper;

    @Autowired
    EvaluationMapper evaluationMapper;

    @Override
    public int createHomework(int uID) {
        //判断权限
        User user = userMapper.getUser(uID);
        if(user.getRole() < 1) { return -1; }

        //创建作业
        Homework homework = new Homework();
        homework.setCreatorID(uID);
        homeworkMapper.createHomework(homework);
        return homework.getHwID();
    }

    @Override
    public int saveHomework(int uID, int hwID, String name, String content) {
        Homework homework = homeworkMapper.getHomework(hwID);
        if(homework == null) { return -2; } //不存在
        if(homework.getCreatorID() != uID) { return -1; } //无权限
        homeworkMapper.saveHomework(hwID, name, content);
        return 0;
    }

    @Override
    public Map<String, Object> getSavedHomework(int uID, int hwID) {
        Map<String, Object> res = new HashMap<>();
        Homework homework = homeworkMapper.getHomework(hwID);
//        int msg = 0;
        if(homework == null) {
            res.put("msg", -2);
            return res;
        } //不存在
        if(homework.getCreatorID() != uID) {
            res.put("msg", -1);
            return res;
        } //无权限
        res.put("content", homework.getContent());
        res.put("msg", 0);
        res.put("name", homework.getHwName());
        return res;
    }

    @Override
    public List<Map<String, Object>> listHomework(int uID) {
        List<Homework> homeworks = homeworkMapper.listHomework(uID);
        List<Map<String, Object>> res = new ArrayList<>();
        for(Homework homework : homeworks) {
            Map<String, Object> m = new HashMap<>();
            m.put("hwID", homework.getHwID());
            m.put("name", homework.getHwName());
            m.put("cTime", homework.getCTime().format(DateTimeFormatter.ISO_DATE_TIME));
            m.put("isPublish", homework.isPublish());
            m.put("isStartEvaluation", homework.isStartEva());
//            List<HomeworkMember> studentHomeworks = homeworkMapper.getStudentHomeworks(homework.getHwID());
            m.put("cID", homework.getCID());
            if(homework.getDdl() != null) { m.put("ddl", homework.getDdl().format(DateTimeFormatter.ISO_DATE_TIME)); }
            else { m.put("ddl", null); }
            res.add(m);
        }
        return res;
    }

    @Override
    public int publishHomework(int uID, int hwID, List<Integer> classes, LocalDateTime ddl, String content, String name, int score) {
        Homework homework = homeworkMapper.getHomework(hwID);
        User user = userMapper.getUser(uID);
        if(homework == null) { return -1; } //找不到作业

        //判断权限
        if(uID != homework.getCreatorID()) { return -2; } //无权限

        //判断班级是否合法
        for(int cID : classes) {
            Integer role = classMapper.getRole(uID, cID);
            if(role == null || role < 2) { return -3; } //无权限发布作业
        }

        //判断ddl是否合法
        if(ddl.isBefore(LocalDateTime.now())) { return -4; } //ddl不正确

        //发布作业
        homeworkMapper.publish(hwID, ddl, content, name, score, classes.get(0));
//        List<ClassMember> members = new ArrayList<>();
        if(!homework.isPublish()) {
            for (int cID : classes) {
                List<ClassMember> member = classMapper.listMembers(cID);
                Classes c = classMapper.getClasses(cID);
                String nContent = user.getName() + "在班级：" + c.getClassName() + " 发布了作业：" + homework.getHwName() + "，请注意查收并按时完成。";
                for (ClassMember cm : member) {
                    if (cm.getRole() < 2) {
                        homeworkMapper.addHomework(cm.getUID(), hwID, cID); //为每一位学生创建作业
                        notationMapper.createNote(user.getName(), c.getClassName(), nContent, cm.getUID()); //为每一位学生创建公共
                    }
                }
            }
        }

        return 0;
    }

    @Override
    public List<Map<String, Object>> getHomework(int uID, Integer cID) {
        List<Map<String, Object>> res = new ArrayList<>();
        List<HomeworkMember> homeworks = homeworkMapper.getMyHomework(uID, cID);
        for(HomeworkMember hm : homeworks) {
            Homework homework = homeworkMapper.getHomework(hm.getHwID());
            Map<String, Object> m = new HashMap<>();
            m.put("hwID", hm.getHwID());
            m.put("isSubmit", hm.isSubmit());
            m.put("ddl", homework.getDdl().format(DateTimeFormatter.ISO_DATE_TIME));
            m.put("cTime", homework.getCTime().format(DateTimeFormatter.ISO_DATE_TIME));
            m.put("creator", userMapper.getUser(homework.getCreatorID()).getName());
            m.put("name", homework.getHwName());
            m.put("isStartEvaluation", homework.isStartEva());
            m.put("score", hm.getScore());
            //获取成绩
            EvaluationStat myEvaluation = evaluationMapper.getMyEvaluation(uID, hm.getHwID());
            if(myEvaluation != null && myEvaluation.getScore() != -1) {
                m.put("evaScore", myEvaluation.getScore());
            }
            res.add(m);
        }
        return res;
    }

    @Override
    public int saveHomework_stu(int uID, int hwID, String content) {
        HomeworkMember hm = homeworkMapper.getHomework_stu(uID, hwID);
        if(hm == null) { return -1; }
        if(hm.isSubmit()) { return -2; }
        homeworkMapper.saveHomework_stu(uID, hwID, content);
        return 0;
    }

    @Override
    public Map<String, Object> getSavedHomework_stu(int uID, int hwID) {
        HomeworkMember hm = homeworkMapper.getHomework_stu(uID, hwID);
        Homework homework = homeworkMapper.getHomework(hwID);
        Map<String, Object> res = new HashMap<>();
        if(hm == null) {
            res.put("state", -1);
            return res;
        }
        if(homework == null) {
            res.put("state", -2);
            return res;
        }
        res.put("state", 0);
        res.put("name", homework.getHwName());
        res.put("homework", homework.getContent());
        res.put("content", hm.getContent());
        return res;
    }

    @Override
    public int submitHomework(int uID, int hwID, String content) {
        HomeworkMember hm = homeworkMapper.getHomework_stu(uID, hwID);
        if(hm == null) { return -1; }
        if(hm.isSubmit()) { return -2; }
        homeworkMapper.submitHomework(uID, hwID, content);
        return 0;
    }

    //获取学生作业情况
    @Override
    public List<Map<String, Object>> getStudentHomeworks(int uID, int hwID) {
        List<Map<String, Object>> res = new ArrayList<>();
        Homework homework = homeworkMapper.getHomework(hwID);
        if(homework == null || homework.getCreatorID() != uID) {
            return res;
        }
        List<HomeworkMember> homeworkMembers = homeworkMapper.getStudentHomeworks(hwID);
        for(HomeworkMember homeworkMember : homeworkMembers) {
            Map<String, Object> m = new HashMap<>();
            User user = userMapper.getUser(homeworkMember.getUID());
            Classes classes = classMapper.getClasses(homeworkMember.getCID());
            m.put("uID", homeworkMember.getUID());
            m.put("uName", user.getName());
            m.put("uHead", user.getHead());
            m.put("isSubmit", homeworkMember.isSubmit());
            m.put("cID", homeworkMember.getCID());
            m.put("cName", classes.getClassName());
            m.put("score", homeworkMember.getScore());
            EvaluationStat myEvaluation = evaluationMapper.getMyEvaluation(homeworkMember.getUID(), homeworkMember.getHwID());
            if(myEvaluation != null && myEvaluation.getScore() != -1) {
                m.put("evaScore", myEvaluation.getScore());
            }
            res.add(m);
        }
        return res;
    }

    @Override
    public Map<String, Object> getStudentHomework(int uID, int hwID, int sID) {
        Map<String, Object> res = new HashMap<>();
        res.put("hwName", null);
        res.put("hwContent", null);
        res.put("content", null);
        Homework homework = homeworkMapper.getHomework(hwID);
        if(homework == null || homework.getCreatorID() != uID) {
            return res;
        }
        HomeworkMember homeworkMember = homeworkMapper.getHomework_stu(sID, hwID);
        if(!homeworkMember.isSubmit()) { return res; }
        res.put("hwName", homework.getHwName());
        res.put("hwContent", homework.getContent());
        res.put("content", homeworkMember.getContent());
        return res;
    }

    @Override
    public Map<String, Object> getSubmitNum(int hwID) {
        Integer homeworkMemberNum = homeworkMapper.getHomeworkMemberNum(hwID);
        Integer submitNum = homeworkMapper.getSubmitNum(hwID);
        Map<String, Object> res = new HashMap<>();
        res.put("sum", homeworkMemberNum);
        res.put("submitNum", submitNum);
        return res;
    }

    @Override
    public List<Map<String, Object>> getAbsent(int uID, int hwID) {
        List<Map<String, Object>> res = new ArrayList<>();
        Homework homework = homeworkMapper.getHomework(hwID);
        User user = userMapper.getUser(uID);
        if (homework == null || uID != homework.getCreatorID()) {
            return res;
        }
        List<HomeworkMember> homeworkMemberList = homeworkMapper.getAbsent(hwID);
        for(HomeworkMember homeworkMember : homeworkMemberList) {
            Map<String, Object> m = new HashMap<>();
            m.put("sID", homeworkMember.getUID());
            User u = userMapper.getUser(homeworkMember.getUID());
            m.put("head", u.getHead());
            m.put("name", u.getName());
        }
        return res;
    }

    @Override
    public int mark(int uID, int hwID, int sID, int score, String comment) {
        homeworkMapper.mark(sID, hwID, score, comment);
        return 0;
    }
}
