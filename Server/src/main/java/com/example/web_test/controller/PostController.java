package com.example.web_test.controller;

import com.example.web_test.pojo.Result;
import com.example.web_test.server.PostServer;
import com.example.web_test.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class PostController {

    @Autowired
    PostServer postServer;

    //创建提问
    @PostMapping("/createPost")
    public Result createPost(HttpServletRequest request) {
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        int uID = (int) claims.get("ID");
        int cID = Integer.parseInt(request.getParameter("cID"));
        String content = request.getParameter("content");
        String pName = request.getParameter("name");
        int teacherOnly = Integer.parseInt(request.getParameter("teacherOnly")); //0代表所有人可见，1代表只有老师可见
        boolean isTeacherOnly = teacherOnly != 0;

        int res = postServer.createPost(uID, cID, content, pName, isTeacherOnly);

        return Result.success();
    }

    //获取所有提问
    @PostMapping("/getPosts")
    public Result getPosts(HttpServletRequest request) {
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        int uID = (int) claims.get("ID");
        int cID = Integer.parseInt(request.getParameter("cID"));
        List<Map<String, Object>> res = postServer.getPosts(uID, cID);
        return Result.success(res);
    }

    //回复帖子
    @PostMapping("/reply")
    public Result reply(HttpServletRequest request) {
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        int uID = (int) claims.get("ID");
        int pID = Integer.parseInt(request.getParameter("pID"));
        String content = request.getParameter("content");
        int res = postServer.reply(uID, pID, content);
        return Result.success();
    }

    @PostMapping("/getReply")
    public Result getReply(HttpServletRequest request) {
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        int uID = (int) claims.get("ID");
        int pID = Integer.parseInt(request.getParameter("pID"));
        Map<String, Object> res = postServer.getReply(uID, pID);
        return Result.success(res);
    }

}
