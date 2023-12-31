package com.example.web_test.server.impl;

import com.example.web_test.mapper.*;
import com.example.web_test.pojo.*;
import com.example.web_test.server.HomeworkServer;
import com.example.web_test.utils.DuplicateCheck.DuplicateCheckUtils;
import com.example.web_test.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class HomeworkServerA implements HomeworkServer {

    @Value("${path.duplicateImgPath}")
    private String imgPath;

    @Value("${threshold.text}")
    private int textThreshold;

    @Value("${threshold.code}")
    private int codeThreshold;

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
        if (user.getRole() < 1) {
            return -1;
        }

        //创建作业
        Homework homework = new Homework();
        homework.setCreatorID(uID);
        homeworkMapper.createHomework(homework);
        return homework.getHwID();
    }

    @Override
    public int saveHomework(int uID, int hwID, String name, String content) {
        Homework homework = homeworkMapper.getHomework(hwID);
        if (homework == null) {
            return -2;
        } //不存在
        if (homework.getCreatorID() != uID) {
            return -1;
        } //无权限
        homeworkMapper.saveHomework(hwID, name, content);
        return 0;
    }

    @Override
    public Map<String, Object> getSavedHomework(int uID, int hwID) {
        Map<String, Object> res = new HashMap<>();
        Homework homework = homeworkMapper.getHomework(hwID);
//        int msg = 0;
        if (homework == null) {
            res.put("msg", -2);
            return res;
        } //不存在
        if (homework.getCreatorID() != uID) {
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
        for (Homework homework : homeworks) {
            Map<String, Object> m = new HashMap<>();
            m.put("hwID", homework.getHwID());
            m.put("name", homework.getHwName());
            m.put("cTime", homework.getCTime().format(DateTimeFormatter.ISO_DATE_TIME));
            m.put("isPublish", homework.isPublish());
            m.put("isStartEvaluation", homework.isStartEva());
//            List<HomeworkMember> studentHomeworks = homeworkMapper.getStudentHomeworks(homework.getHwID());
            m.put("cID", homework.getCID());
            if (homework.getDdl() != null) {
                m.put("ddl", homework.getDdl().format(DateTimeFormatter.ISO_DATE_TIME));
            } else {
                m.put("ddl", null);
            }
            res.add(m);
        }
        return res;
    }

    @Override
    public int publishHomework(int uID, int hwID, List<Integer> classes, LocalDateTime ddl, String content, String name, int score) {
        Homework homework = homeworkMapper.getHomework(hwID);
        User user = userMapper.getUser(uID);
        if (homework == null) {
            return -1;
        } //找不到作业

        //判断权限
        if (uID != homework.getCreatorID()) {
            return -2;
        } //无权限

        //判断班级是否合法
        for (int cID : classes) {
            Integer role = classMapper.getRole(uID, cID);
            if (role == null || role < 2) {
                return -3;
            } //无权限发布作业
        }

        //判断ddl是否合法
        if (ddl.isBefore(LocalDateTime.now())) {
            return -4;
        } //ddl不正确

        //发布作业
        homeworkMapper.publish(hwID, ddl, content, name, score, classes.get(0));
//        List<ClassMember> members = new ArrayList<>();
        if (!homework.isPublish()) {
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
        for (HomeworkMember hm : homeworks) {
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
            if (myEvaluation != null && myEvaluation.getScore() != -1) {
                m.put("evaScore", myEvaluation.getScore());
            }
            res.add(m);
        }
        return res;
    }

    @Override
    public int saveHomework_stu(int uID, int hwID, String content) {
        HomeworkMember hm = homeworkMapper.getHomework_stu(uID, hwID);
        if (hm == null) {
            return -1;
        }
        if (hm.isSubmit()) {
            return -2;
        }
        homeworkMapper.saveHomework_stu(uID, hwID, content);
        return 0;
    }

    @Override
    public Map<String, Object> getSavedHomework_stu(int uID, int hwID) {
        HomeworkMember hm = homeworkMapper.getHomework_stu(uID, hwID);
        Homework homework = homeworkMapper.getHomework(hwID);
        Map<String, Object> res = new HashMap<>();
        if (hm == null) {
            res.put("state", -1);
            return res;
        }
        if (homework == null) {
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
        if (hm == null) {
            return -1;
        }
        if (hm.isSubmit()) {
            return -2;
        }
        homeworkMapper.submitHomework(uID, hwID, content);
        return 0;
    }

    //获取学生作业情况
    @Override
    public List<Map<String, Object>> getStudentHomeworks(int uID, int hwID) {
        List<Map<String, Object>> res = new ArrayList<>();
        Homework homework = homeworkMapper.getHomework(hwID);
        if (homework == null || homework.getCreatorID() != uID) {
            return res;
        }
        List<HomeworkMember> homeworkMembers = homeworkMapper.getStudentHomeworks(hwID);
        for (HomeworkMember homeworkMember : homeworkMembers) {
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
            if (myEvaluation != null && myEvaluation.getScore() != -1) {
                m.put("evaScore", myEvaluation.getScore());
            }
            List<Appeal> appeal = homeworkMapper.getMyAppeal(homeworkMember.getUID(), hwID);
            if(appeal.size() != 0 && !appeal.get(0).isHandled()) {
                m.put("isAppeal", true);
            } else {
                m.put("isAppeal", false);
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
        if (homework == null || homework.getCreatorID() != uID) {
            return res;
        }
        HomeworkMember homeworkMember = homeworkMapper.getHomework_stu(sID, hwID);
        if (!homeworkMember.isSubmit()) {
            return res;
        }
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
        for (HomeworkMember homeworkMember : homeworkMemberList) {
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

    @Override
    public int appeal(int uID, int hwID, String content) {
        Homework homework = homeworkMapper.getHomework(hwID);
        HomeworkMember homeworkStu = homeworkMapper.getHomework_stu(uID, hwID);
        EvaluationStat myEvaluation = evaluationMapper.getMyEvaluation(uID, hwID);
        if (homework == null || homeworkStu == null) {
            return -1; //无权限
        }
        if (myEvaluation == null) {
            return -2; //当前阶段无法申诉
        }
        List<Appeal> myAppeal = homeworkMapper.getMyAppeal(uID, hwID);
        if(myAppeal.size() != 0) {
            return -3; //无法多次申诉
        }

        //创建申诉
        homeworkMapper.appeal(uID, hwID, homework.getCID(), content);
        return 0;
    }

    @Override
    public List<Map<String, Object>> getAppeal(int uID, int hwID) {
        List<Map<String, Object>> res = new ArrayList<>();
        List<Appeal> appeals = homeworkMapper.getAppeal(hwID);

        for(Appeal appeal : appeals) {
            Map<String, Object> m = new HashMap<>();
            User user = userMapper.getUser(appeal.getUID());
            m.put("uID", appeal.getUID());
            m.put("apID", appeal.getApID());
            m.put("content", appeal.getContent());
            m.put("head", user.getHead());
            m.put("uName", user.getName());
            m.put("isHandled", appeal.isHandled());
            res.add(m);
        }
        return res;
    }

    @Override
    public int handleAppeal(int uID, int hwID, int sID, String result) {
        homeworkMapper.handleAppeal(hwID, sID, result);

        //发通知
        Appeal appeal = homeworkMapper.getAppealByID(hwID, sID);
        if(appeal == null) { return -1; }
        User user = userMapper.getUser(uID);
        Classes classes = classMapper.getClasses(appeal.getCID());
        Homework homework = homeworkMapper.getHomework(appeal.getHwID());
        String content = "您对作业：“" + homework.getHwName() + "”的申诉已被处理完成，处理结果为：" + result;
        notationMapper.createNote(user.getName(), classes.getClassName(), content, appeal.getUID());
        return 0;
    }

    @Override
    public String analyseGrade(int uID, int cID) {
        List<Integer> grades = new ArrayList<>();
        List<HomeworkMember> myHomework = homeworkMapper.getMyHomework(uID, cID);
        StringBuilder res = new StringBuilder(classMapper.getClasses(cID).getClassName() + "：");
        for(HomeworkMember homeworkMember : myHomework) {
            EvaluationStat myEvaluation = evaluationMapper.getMyEvaluation(uID, homeworkMember.getHwID());
            if(/*evaluationMapper.getEvaluation(homeworkMember.getHwID()).getDdl().isBefore(LocalDateTime.now()) &&*/myEvaluation != null && myEvaluation.getScore() != -1) {
                grades.add(myEvaluation.getScore());
                res.append(myEvaluation.getScore()).append(",");
            }
        }
        return res.toString();
//        String reply;
//        try {
//            reply = AnalyseUtils.getReply(res.toString());
//        } catch (IOException e) {
//            return null;
//        }
//        return reply;
    }

    @Override
    public List<Integer> getGrades(int uID, int cID) {
        List<Integer> grades = new ArrayList<>();
        List<HomeworkMember> myHomework = homeworkMapper.getMyHomework(uID, cID);
        for(HomeworkMember homeworkMember : myHomework) {
            EvaluationStat myEvaluation = evaluationMapper.getMyEvaluation(uID, homeworkMember.getHwID());
            if(evaluationMapper.getEvaluation(homeworkMember.getHwID()).getDdl().isBefore(LocalDateTime.now()) && myEvaluation.getScore() != -1) {
                grades.add(myEvaluation.getScore());
            }
        }
        return grades;
    }

    @Override
    @Async
    public CompletableFuture<Result> startTask() {
        // 模拟一个异步任务，可以是任何耗时的操作
        try {
            Thread.sleep(5000); // 休眠5秒钟，模拟耗时操作
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Async task completed");
        return CompletableFuture.completedFuture(Result.success());
    }

    @Override
    public boolean canStartDuplicateCheck(int uID, int hwID) {
        //判断权限
        Homework homework = homeworkMapper.getHomework(hwID);
        if(homework.getCreatorID() != uID) { return false; }

        Integer duplicateState = homeworkMapper.getDuplicateState(hwID);
        return duplicateState != 1;
    }

    @Override
    @Async
    public void startDuplicateCheck(int hwID) {
        homeworkMapper.startCheck(hwID);
        homeworkMapper.deleteCheckResult(hwID);
        //获取所有作业
        List<HomeworkMember> studentHomeworks = homeworkMapper.getStudentHomeworks(hwID);
        for(int k = 0; k < studentHomeworks.size(); k++) {
            for(int l = k + 1; l < studentHomeworks.size(); l++) {
                HomeworkMember hw1 = studentHomeworks.get(k);
                HomeworkMember hw2 = studentHomeworks.get(l);
                if(!hw1.isSubmit() || !hw2.isSubmit()) { continue; }
//                if(hw1.getUID() == hw2.getUID()) { continue; }
                StringBuilder result = new StringBuilder();
                StringBuilder detail = new StringBuilder();
                User user1 = userMapper.getUser(hw1.getUID());
                User user2 = userMapper.getUser(hw2.getUID());
                detail.append("# 学生 " + user1.getName() + " 与学生 " + user2.getName() + " 的查重报告\n\n");

                //文字查重
                detail.append("## 文字查重\n");
                String str1 = TextUtils.deleteHead(hw1.getContent());
                String str2 = TextUtils.deleteHead(hw2.getContent());
                str1 = TextUtils.extractMainContent(str1);
                str2 = TextUtils.extractMainContent(str2);
                if(str1.length() < 50 || str2.length() < 50) { continue; }
                double rate = DuplicateCheckUtils.getRate(str1, str2);
                System.out.println("同学" + hw1.getUID() + ", " + hw2.getUID() + "文字查重率为" + rate);
                if(rate * 100 > textThreshold) {
                    result.append("文字查重率过高").append("\n");
                    detail.append("文字查重率过高，为：" + (rate*100) + "%").append("\n\n");
                } else {
                    detail.append("无").append("\n\n");
                }

                //代码查重
                detail.append("## 代码查重\n");
                List<String> codes1 = TextUtils.findCode(hw1.getContent());
                List<String> codes2 = TextUtils.findCode(hw2.getContent());
                for(int i = 0; i < codes1.size(); i++) {
                    for(int j = 0; j < codes2.size(); j++) {
                        int codeRate = DuplicateCheckUtils.checkCode(codes1.get(i), codes2.get(j));
                        System.out.println("同学" + hw1.getUID() + "第" + i + "个, " + hw2.getUID() + "第" + j + "个代码查重率为" + codeRate);
                        if(codeRate > codeThreshold) {
                            result.append("前者第").append(i + 1).append("个代码块和后者第").append(j + 1).append("个代码块查重率过高，查重率为").append(codeRate).append('%').append("\n");
                            detail.append("**").append("前者第").append(i + 1).append("个代码块和后者第").append(j + 1).append("个代码块查重率过高，查重率为").append(codeRate).append('%').append("**\n\n");
                            detail.append("**详情如下：**\n\n");
                            detail.append(user1.getName()).append("的第").append(i + 1).append("个代码块：\n");
                            detail.append("```\n").append(codes1.get(i)).append("\n```\n");
                            detail.append(user2.getName()).append("的第").append(j + 1).append("个代码块：\n");
                            detail.append("```\n").append(codes2.get(j)).append("\n```\n");
                        }
                    }
                }

                //图片查重
                detail.append("\n## 图片查重\n");
                List<String> imgsOl1 = TextUtils.extractImageLinks(hw1.getContent());
                List<String> imgsOl2 = TextUtils.extractImageLinks(hw2.getContent());
                List<String> imgs1 = TextUtils.downloadImages(TextUtils.extractImageLinks(hw1.getContent()), imgPath);
                List<String> imgs2 = TextUtils.downloadImages(TextUtils.extractImageLinks(hw2.getContent()), imgPath);
                for(int i = 0; i < imgs1.size(); i++) {
                    for(int j = 0; j < imgs2.size(); j++) {
                        if(DuplicateCheckUtils.checkImg(imgs1.get(i), imgs2.get(j))) {
                            System.out.println("同学" + hw1.getUID() + "第" + i + "个, " + hw2.getUID() + "第" + j + "个图片重复度高");
                            result.append("前者第").append(i + 1).append("个图片和后者第").append(j + 1).append("个图片相似度过高").append("\n");

                            detail.append("**").append("前者第").append(i + 1).append("个图片和后者第").append(j + 1).append("个图片被认定为相似").append("**\n\n");
                            detail.append("**详情如下：**\n\n");
                            detail.append(user1.getName()).append("的第").append(i + 1).append("个图片：\n");
                            detail.append("![img1](").append(imgsOl1.get(i)).append(")\n\n");
                            detail.append(user2.getName()).append("的第").append(j + 1).append("个图片：\n");
                            detail.append("![img2](").append(imgsOl2.get(j)).append(")\n\n");
                        }
                    }
                }
                TextUtils.deleteImages(imgs1);
                TextUtils.deleteImages(imgs2);

                //保存结果
                if(result.length() != 0) {
                    homeworkMapper.addCheckResult(hwID, hw1.getUID(), hw2.getUID(), result.toString(), detail.toString());
                }
            }
        }
        homeworkMapper.endDuplicate(hwID);
    }

    @Override
    public int getDuplicateState(int uID, int hwID) {
        //判断权限
        Homework homework = homeworkMapper.getHomework(hwID);
        if(homework.getCreatorID() != uID) { return -1; }

        return homeworkMapper.getDuplicateState(hwID);
    }

    @Override
    public List<Map<String, Object>> getDuplicateInfo(int uID, int hwID) {
        List<Map<String, Object>> res = new ArrayList<>();
        //判断权限
        Homework homework = homeworkMapper.getHomework(hwID);
        if(homework.getCreatorID() != uID) { return res; }

        List<DuplicateRecord> duplicateRecords = homeworkMapper.getDuplicateInfo(hwID);
        for(DuplicateRecord duplicateRecord : duplicateRecords) {
            Map<String, Object> m = new HashMap<>();
            User user1 = userMapper.getUser(duplicateRecord.getSID1());
            User user2 = userMapper.getUser(duplicateRecord.getSID2());
            m.put("sID1", duplicateRecord.getSID1());
            m.put("sID2", duplicateRecord.getSID2());
            m.put("sName1", user1.getName());
            m.put("sName2", user2.getName());
            m.put("content", duplicateRecord.getDescription());
            res.add(m);
        }

        return res;
    }

    @Override
    public String getDuplicateDetail(int uID, int hwID, int sID1, int sID2) {
        Homework homework = homeworkMapper.getHomework(hwID);
        if(homework.getCreatorID() != uID) { return ""; }

        String res = homeworkMapper.getDuplicateDetail(hwID, sID1, sID2);
        if(res == null) {
            res = homeworkMapper.getDuplicateDetail(hwID, sID2, sID1);
            if(res == null) { return ""; }
        }
        return res;
    }
}
