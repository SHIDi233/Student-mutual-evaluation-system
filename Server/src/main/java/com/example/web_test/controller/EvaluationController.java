package com.example.web_test.controller;

import com.example.web_test.pojo.Result;
import com.example.web_test.server.EvaluationServer;
import com.example.web_test.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
public class EvaluationController {

    @Autowired
    EvaluationServer evaluationServer;

    //为某一作业开启互评
    @PostMapping("/startEvaluation")
    public Result startEvaluation(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");

        int hwID = Integer.parseInt(request.getParameter("hwID"));
        String ddls = request.getParameter("ddl");
        LocalDateTime ddl = LocalDateTime.parse(ddls, DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));

        int res = evaluationServer.startEvaluation(uID, hwID, ddl);
        if(res == -1) { return Result.error("您无权执行此操作"); }
        if(res == -2) { return Result.error("互评已经开始"); }
        if(res == -3) { return Result.error("作业未截止前无法开启互评"); }
        if(res == -4) { return Result.error("截止时间设置错误"); }
        if(res == -5) { return Result.error("提交作业人数过少，无法互评"); }
        return Result.success();
    }

    //获取互评
    @PostMapping("/getEvaluation")
    public Result getEvaluation(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");
        int hwID = Integer.parseInt(request.getParameter("hwID"));

        Map<String, Object> res = evaluationServer.getEvaluation(uID, hwID);
        if(res == null) { return Result.error("没有作业可以评"); }
        return Result.success(res);
    }

    //评分
    @PostMapping("/evaluate")
    public Result evaluate(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");
        int eID = Integer.parseInt(request.getParameter("eID"));
        String comment = request.getParameter("comment");
        int score = Integer.parseInt(request.getParameter("score"));

        int res = evaluationServer.evaluate(uID, eID, score, comment);
        if(res == -1) { return Result.error("您没有权限执行此操作"); }
        return Result.success();
    }

    //公布成绩
    @PostMapping("/calculateScore")
    public Result calculateScore(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");
        int hwID = Integer.parseInt(request.getParameter("hwID"));

        int res = evaluationServer.calculateScore(uID, hwID);
        if(res == -1) { return Result.error("您没有权限执行此操作"); }
        return Result.success();
    }

    //老师查看成绩排名
    @PostMapping("/getRank")
    public Result getRank(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");
        int hwID = Integer.parseInt(request.getParameter("hwID"));

        List<Map<String, Object>> res = evaluationServer.getRank(uID, hwID);
        return Result.success(res);
    }


}
