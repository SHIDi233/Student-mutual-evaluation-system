package com.example.web_test.server;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface EvaluationServer {

    int startEvaluation(int uID, int hwID, LocalDateTime ddl);

    Map<String, Object> getEvaluation(int uID, int hwID);

    int evaluate(int uID, int eID, int score, String comment, String image);

    int calculateScore(int uID, int hwID);

    List<Map<String, Object>> getRank(int uID, int hwID);

    List<Map<String, Object>> getEvaluatedMember(int uID, int hwID, int sID);

    Map<String, Object> getEvaluationResult(int uID, int eID);

    int modifyScore(int uID, int sID, int hwID, int score);
}
