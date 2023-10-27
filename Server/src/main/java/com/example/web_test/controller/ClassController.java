package com.example.web_test.controller;

import com.example.web_test.pojo.Result;
import com.example.web_test.server.ClassServer;
import com.example.web_test.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class ClassController {

    @Autowired
    ClassServer classServer;

    @GetMapping("/getClasses")
    public Result getClasses(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");
        List<Map<String, Object>> classes = classServer.getClasses(uID);
        return Result.success(classes);
    }

    @PostMapping("/createClass")
    public Result createClass(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");

        String className = request.getParameter("className");
        String introduction = request.getParameter("introduction");
        String subject = request.getParameter("subject");
        int res = classServer.createClass(uID, className, subject, introduction);
        if(res == 0) {
            return Result.success("班级创建成功");
        }
        return Result.error("您无权创建班级");
    }

    @GetMapping("/getCode")
    public Result getCode(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");

        int cID = Integer.parseInt(request.getParameter("classID"));
        Map<String, Object> code = classServer.getCode(cID);
        return Result.success(code);
    }

    @PostMapping("/searchClass")
    public Result searchClass(String code) {
        Map<String, Object> res = classServer.searchClass(code);
        if(res == null) {
            return Result.error("找不到班级");
        }
        return Result.success(res);
    }

    @PostMapping("/joinClass")
    public Result joinClass(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");

        int cID = Integer.parseInt(request.getParameter("classID"));

        int i = classServer.joinClass(uID, cID);
        if(i == 1) { return Result.error("找不到班级"); }
        if(i == -2) { return Result.error("您已经在班级中"); }
        return Result.success();
    }

    @PostMapping("/listMember")
    public Result listMember(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");
        int cID = Integer.parseInt(request.getParameter("classID"));
        List<Map<String, Object>> res = classServer.listMembers(uID, cID);
        return Result.success(res);
    }

    @PostMapping("/kickMember")
    public Result kickMember(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");
        int cID = Integer.parseInt(request.getParameter("classID"));
        int kID = Integer.parseInt(request.getParameter("uID"));
        if(uID == kID) {
            return Result.error("您不能删除您自己");
        }
        int i = classServer.kickMember(uID, cID, kID);
        if(i != 0) {
            return Result.error("您没有执行此操作的权限");
        }
        return Result.success();
    }

    @PostMapping("/deleteClass")
    public Result deleteClass(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");
        int cID = Integer.parseInt(request.getParameter("classID"));
        int i = classServer.deleteClass(uID, cID);
        if(i == -1) { return Result.error("您没有权限删除该班级"); }
        return Result.success();
    }

    @PostMapping("/approveTeacher")
    public Result approveTeacher(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");
        return null;
    }
}
