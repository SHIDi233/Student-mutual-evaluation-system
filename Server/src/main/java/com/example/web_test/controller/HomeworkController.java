package com.example.web_test.controller;

import com.example.web_test.pojo.Result;
import com.example.web_test.server.HomeworkServer;
import com.example.web_test.utils.JwtUtils;
import com.example.web_test.utils.OBSUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class HomeworkController {

    @Autowired
    HomeworkServer homeworkServer;

    @GetMapping("/createHomework")
    public Result createHomework(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");

        int res = homeworkServer.createHomework(uID);
        if(res < 0) { return Result.error("您没有此权限"); }
        return Result.success(res);
    }

    @PostMapping("/saveHomework")
    public Result saveHomework(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");

        int hwID = Integer.parseInt(request.getParameter("hwID"));
        String name = request.getParameter("name");
        String content = request.getParameter("content");
        int i = homeworkServer.saveHomework(uID, hwID, name, content);
        if(i == -2) { return Result.error("作业不存在"); }
        if(i == -1) { return Result.error("您没有权限进行此操作"); }
        return Result.success();
    }

    @PostMapping("/getSavedHomework")
    public Result getSavedHomework(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");

        int hwID = Integer.parseInt(request.getParameter("hwID"));
        Map<String, Object> res = homeworkServer.getSavedHomework(uID, hwID);
        if((int) res.get("msg") == -2) { return Result.error("作业不存在"); }
        if((int) res.get("msg") == -1) { return Result.error("您没有权限进行此操作"); }
        res.remove("msg");
        return Result.success(res);
    }

    //获取创建的作业列表
    @PostMapping("/listHomework")
    public Result listHomework(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");

        List<Map<String, Object>> res = homeworkServer.listHomework(uID);
        return Result.success(res);
    }

    @PostMapping("/publishHomework")
    public Result publishHomework(HttpServletRequest request, @RequestParam Map<String, Object> map) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");

        int hwID = Integer.parseInt((String) map.get("hwID"));
        int score = Integer.parseInt((String) map.get("score"));
//        System.out.println(map.get("classes"));
        String classesString = (String) map.get("classes");
        String[] classesStrs = classesString.split(",");
        List<Integer> classes = new ArrayList<>();
        for(String s : classesStrs) {
            classes.add(Integer.parseInt(s));
        }
        String ddls = (String) map.get("ddl");
        String content = (String) map.get("content");
        String name = (String) map.get("name");
        LocalDateTime ddl = LocalDateTime.parse(ddls, DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));

        int res = homeworkServer.publishHomework(uID, hwID, classes, ddl, content, name, score);
        if(res == -1) { return Result.error("找不到该作业"); }
        if(res == -2) { return Result.error("您没有执行此操作的权限"); }
        if(res == -3) { return Result.error("班级错误"); }
        if(res == -4) { return Result.error("ddl错误"); }
        return Result.success();
    }

    @PostMapping("/getHomework")
    public Result getHomework(HttpServletRequest request, Integer cID) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");

//        int cID = Integer.parseInt(request.getParameter("cID"));
        List<Map<String, Object>> res = homeworkServer.getHomework(uID, cID);
        return Result.success(res);
    }

    //保存作业草稿
    @PostMapping("/saveHomework-stu")
    public Result saveHomework_stu(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");

        int hwID = Integer.parseInt(request.getParameter("hwID"));
        String content = request.getParameter("content");
        int res = homeworkServer.saveHomework_stu(uID, hwID, content);
        if(res == -1) { return Result.error("找不到作业"); }
        if(res == -2) { return Result.error("作业已提交"); }
        return Result.success();
    }

    //获取学生保存的草稿
    @PostMapping("/getSavedHomework-stu")
    public Result  getSavedHomework_stu(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");

        int hwID = Integer.parseInt(request.getParameter("hwID"));
        Map<String, Object> res = homeworkServer.getSavedHomework_stu(uID, hwID);
        if((Integer) res.get("state") < 0) { return Result.error("找不到作业"); }
        res.remove("state");
        return Result.success(res);
    }

    //提交作业
    @PostMapping("/submitHomework")
    public Result submitHomework(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");

        int hwID = Integer.parseInt(request.getParameter("hwID"));
        String content = request.getParameter("content");
        int res = homeworkServer.submitHomework(uID, hwID, content);
        if(res == -1) { return Result.error("找不到作业"); }
        if(res == -2) { return Result.error("无法重复提交"); }
        return Result.success();
    }

    @PostMapping("/uploadFile")
    public Result uploadHeader(HttpServletRequest request) throws IOException {
        //获取文件
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");
        assert file != null;
        String originalFileName = file.getOriginalFilename();
        assert originalFileName != null;
        String newFileName;
        if(originalFileName.contains(".")) {
            newFileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));
        } else {
            newFileName = UUID.randomUUID().toString();
        }
        file.transferTo(new File("D:/vue/source/" + newFileName));
        String filePath = OBSUtils.uploadFile(new File("D:/vue/source/" + newFileName), newFileName);
//        loginServer.setHeader(uID, filePath);
        return Result.success(filePath);
    }

    //获取学生提交的作业
    @PostMapping("/listStudentHomeworks")
    public Result getStudentHomeworks(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");

        int hwID = Integer.parseInt(request.getParameter("hwID"));

        List<Map<String, Object>> res = homeworkServer.getStudentHomeworks(uID, hwID);
        return Result.success(res);
    }

    @PostMapping("/getStudentHomework")
    public Result getStudentHomework(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");

        int hwID = Integer.parseInt(request.getParameter("hwID"));
        int sID = Integer.parseInt(request.getParameter("sID"));

        Map<String, Object> res = homeworkServer.getStudentHomework(uID, hwID, sID);
        return Result.success(res);
    }

    //获取作业提交人数
    @PostMapping("/getSubmitNum")
    public Result getSubmitNum(HttpServletRequest request) {
        int hwID = Integer.parseInt(request.getParameter("hwID"));
        Map<String, Object> res = homeworkServer.getSubmitNum(hwID);
        return Result.success(res);
    }

    //获取未提交作业的人
    @PostMapping("/getAbsent")
    public Result getAbsent(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");

        int hwID = Integer.parseInt(request.getParameter("hwID"));
        List<Map<String, Object>> res = homeworkServer.getAbsent(uID, hwID);
        return Result.success(res);
    }

    //老师批改作业
    @PostMapping("/mark")
    public Result mark(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");
        int hwID = Integer.parseInt(request.getParameter("hwID"));
        int sID = Integer.parseInt(request.getParameter("sID"));
        int score = Integer.parseInt(request.getParameter("score"));
        String comment = request.getParameter("comment");
        homeworkServer.mark(uID, hwID, sID, score, comment);
        return null;
    }

}
