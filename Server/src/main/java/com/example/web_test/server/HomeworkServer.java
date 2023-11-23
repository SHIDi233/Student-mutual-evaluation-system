package com.example.web_test.server;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface HomeworkServer {

    int createHomework(int uID);

    int saveHomework(int uID, int hwID, String name, String content);

    Map<String, Object> getSavedHomework(int uID, int hwID);

    List<Map<String, Object>> listHomework(int uID);

    int publishHomework(int uID, int hwID, List<Integer> classes, LocalDateTime ddl, String content, String name, int score);

    List<Map<String, Object>> getHomework(int uID, Integer cID);

    int saveHomework_stu(int uID, int hwID, String content);

    Map<String, Object> getSavedHomework_stu(int uID, int hwID);

    int submitHomework(int uID, int hwID, String content);

    List<Map<String, Object>> getStudentHomeworks(int uID, int hwID);

    Map<String, Object> getStudentHomework(int uID, int hwID, int sID);

    Map<String, Object> getSubmitNum(int hwID);

    List<Map<String, Object>> getAbsent(int uID, int hwID);

    int mark(int uID, int hwID, int sID, int score, String comment);
}
