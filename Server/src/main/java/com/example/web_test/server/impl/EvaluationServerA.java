package com.example.web_test.server.impl;

import com.example.web_test.mapper.*;
import com.example.web_test.pojo.*;
import com.example.web_test.server.EvaluationServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class EvaluationServerA implements EvaluationServer {

    @Autowired
    HomeworkMapper homeworkMapper;

    @Autowired
    EvaluationMapper evaluationMapper;

    @Autowired
    NotationMapper notationMapper;

    @Autowired
    ClassMapper classMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public int startEvaluation(int uID, int hwID, LocalDateTime ddl) {
        Homework homework = homeworkMapper.getHomework(hwID);
        User user = userMapper.getUser(uID);
        if (homework == null || uID != homework.getCreatorID()) {
            return -1; //无权执行此操作
        }
//        if (homework.isStartEva()) {
//            return -2; //互评已经开始
//        }
        if (LocalDateTime.now().isBefore(homework.getDdl())) {
            //让作业截止
            homeworkMapper.ddl(hwID);
        }
        if (ddl.isBefore(LocalDateTime.now())) {
            return -4; //时间设置错误
        }

        //开始互评
        evaluationMapper.startEva(hwID);
        Evaluation evaluation = evaluationMapper.getEvaluation(hwID);
        if (evaluation != null) {
            evaluationMapper.setEvaDdl(hwID, ddl);
        } else {

            List<HomeworkMember> homeworkMembers = homeworkMapper.getSubmitHomeworks(hwID);
            if(homeworkMembers.size() < 2) { return -5; } //人数过少
            evaluationMapper.addEva(hwID, ddl);
            String content = "老师：" + user.getName() + "开启了作业：" + homework.getHwName() + "的互评，" + "请在截止时间前及时完成。";
            for(HomeworkMember homeworkMember : homeworkMembers) {
                //在数据库中创建互评
                evaluationMapper.createEva(homeworkMember.getUID(), hwID);

                //发通知
                Classes classes = classMapper.getClasses(homeworkMember.getCID());
                notationMapper.createNote(user.getName(), classes.getClassName(), content, homeworkMember.getUID());
            }
        }

        return 0;
    }

    @Override
    public Map<String, Object> getEvaluation(int uID, int hwID) {
        Homework homework = homeworkMapper.getHomework(hwID);
        EvaluationStat evaluationStat = evaluationMapper.getMyEvaluation(uID, hwID);
        if(homework == null || evaluationStat == null) { return null; }


        Map<String, Object> res = new HashMap<>();

        int desID = -1;

        //查看是否有已生成但未评价的作业
        List<EvaluationMember> noEvaluation = evaluationMapper.getNoEvaluation(uID, hwID);
        if(noEvaluation.size() != 0) {
            desID = noEvaluation.get(0).getDesID();
        } else {
            //获取互评
            List<EvaluationStat> members = evaluationMapper.listEvaluations(hwID);
            members.sort(new Comparator<EvaluationStat>() {
                @Override
                public int compare(EvaluationStat o1, EvaluationStat o2) {
                    return o1.getEvaluatedNum() - o2.getEvaluatedNum();
                }
            });
            //寻找未评的作业
            for (EvaluationStat e : members) {
                List<EvaluationMember> evaluationMember = evaluationMapper.getEvaluationMember(uID, e.getUID(), hwID);
                if (evaluationMember.size() == 0 && e.getUID() != uID) {
                    desID = e.getUID();
                    break;
                }
            }
        }
        if(desID == -1) { return null; } //没有能评的作业了
        EvaluationMember evaluationMember = new EvaluationMember();
        evaluationMember.setUID(uID);
        evaluationMember.setDesID(desID);
        evaluationMember.setHwID(hwID);
        if(noEvaluation.size() == 0) {
            evaluationMapper.createEvaluationMember(evaluationMember);
        }

        HomeworkMember desHomework = homeworkMapper.getHomework_stu(desID, hwID);
        if(noEvaluation.size() == 0) {
            res.put("eID", evaluationMember.getEID());
        } else {
            res.put("eID", noEvaluation.get(0).getEID());
        }

        res.put("content", desHomework.getContent());
        res.put("maxScore", homework.getScore());
        return res;
    }

    @Override
    public int evaluate(int uID, int eID, int score, String comment, String image) {
        EvaluationMember evaluationMember = evaluationMapper.getEvaluationMember2(eID, uID);
        if(evaluationMember == null) { return -1; }
        if(evaluationMember.getScore() == null) {
            evaluationMapper.addEvaluateNum(evaluationMember.getUID(), evaluationMember.getHwID());
            evaluationMapper.addEvaluatedNum(evaluationMember.getDesID(), evaluationMember.getHwID());
        }
        evaluationMember.setScore(score);
        evaluationMember.setComment(comment);
        evaluationMember.setImage(image);
        evaluationMapper.evaluate(evaluationMember);
        //计算分数
        calculateScore(evaluationMember.getHwID());
        return 0;
    }

    @Override
    public int calculateScore(int uID, int hwID) {
        Homework homework = homeworkMapper.getHomework(hwID);
        User user = userMapper.getUser(uID);
        if (homework == null || uID != homework.getCreatorID()) {
            return -1; //无权执行此操作
        }
        if(!homework.isStartEva()) {
            return -2; //还未进行互评
        }
        List<EvaluationStat> evaluationStats = evaluationMapper.listEvaluations(hwID);
        for(EvaluationStat evaluationStat : evaluationStats) {
            //获取所有互评结果
            List<EvaluationMember> evaluationMemberList = evaluationMapper.getEvaluationResult(evaluationStat.getUID(), hwID);
            int sum = 0;
            for(EvaluationMember e : evaluationMemberList) {
                sum += e.getScore();
            }
            int score = 0;
            if(evaluationMemberList.size() != 0) {
                score = sum / evaluationMemberList.size();
            }
            //放入数据库
            evaluationMapper.setScore(evaluationStat.getUID(), score, hwID);
        }
        return 0;
    }

    //自动计算分数
    public int calculateScore(int hwID) {
        Homework homework = homeworkMapper.getHomework(hwID);
//        User user = userMapper.getUser(uID);
        if (homework == null) {
            return -1; //无权执行此操作
        }
        if(!homework.isStartEva()) {
            return -2; //还未进行互评
        }
        List<EvaluationStat> evaluationStats = evaluationMapper.listEvaluations(hwID);
        for(EvaluationStat evaluationStat : evaluationStats) {
            //获取所有互评结果
            List<EvaluationMember> evaluationMemberList = evaluationMapper.getEvaluationResult(evaluationStat.getUID(), hwID);
            int sum = 0;
            for(EvaluationMember e : evaluationMemberList) {
                sum += e.getScore();
            }
            int score = 0;
            if(evaluationMemberList.size() != 0) {
                score = sum / evaluationMemberList.size();
            }
            //放入数据库
            evaluationMapper.setScore(evaluationStat.getUID(), score, hwID);
        }
        return 0;
    }

    @Override
    public List<Map<String, Object>> getRank(int uID, int hwID) {
        List<Map<String, Object>> res = new ArrayList<>();
        Homework homework = homeworkMapper.getHomework(hwID);
        User user = userMapper.getUser(uID);
        if (homework == null || uID != homework.getCreatorID()) {
            return res;
        }
        List<EvaluationStat> rank = evaluationMapper.getRank(hwID);
        for(EvaluationStat evaluationStat : rank) {
            Map<String, Object> m = new HashMap<>();
            User u = userMapper.getUser(evaluationStat.getUID());
            m.put("sID", evaluationStat.getUID());
            m.put("head", u.getHead());
            m.put("name", u.getName());
            m.put("score", evaluationStat.getScore());
            res.add(m);
        }
        return res;
    }

    @Override
    public List<Map<String, Object>> getEvaluatedMember(int uID, int hwID, int sID) {
        List<Map<String, Object>> res = new ArrayList<>();
        Homework homework = homeworkMapper.getHomework(hwID);

        //判断是否有权限
        if(homework == null || homework.getCreatorID() != uID) { return res; }

        List<EvaluationMember> evaluatedMembers = evaluationMapper.getEvaluatedMember(hwID, sID);
        for(EvaluationMember evaluationMember : evaluatedMembers) {
            Map<String, Object> m = new HashMap<>();
            m.put("eID", evaluationMember.getEID());
            m.put("uID", evaluationMember.getUID());
            User user = userMapper.getUser(evaluationMember.getUID());
            m.put("header", user.getHead());
            m.put("name", user.getName());
            m.put("score", evaluationMember.getScore());
            res.add(m);
        }
        return res;
    }

    @Override
    public Map<String, Object> getEvaluationResult(int uID, int eID) {
        EvaluationMember evaluationMember = evaluationMapper.getEvaluationMember3(eID);
        Map<String, Object> res = new HashMap<>();
        int hwID = evaluationMember.getHwID();
        int desID = evaluationMember.getDesID();
        HomeworkMember homeworkStu = homeworkMapper.getHomework_stu(desID, hwID);
        res.put("content", homeworkStu.getContent());
        res.put("comment", evaluationMember.getComment());
        res.put("score", evaluationMember.getScore());
        res.put("image", evaluationMember.getImage());
        return res;
    }

    @Override
    public int modifyScore(int uID, int sID, int hwID, int score) {
        evaluationMapper.modifyScore(sID, hwID, score);
        return 0;
    }
}
