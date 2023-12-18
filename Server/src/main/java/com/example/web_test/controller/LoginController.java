package com.example.web_test.controller;

import com.example.web_test.pojo.Result;
import com.example.web_test.pojo.User;
import com.example.web_test.server.LoginServer;
import com.example.web_test.utils.JwtUtils;
import com.example.web_test.utils.OBSUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private LoginServer loginServer;

    @Value("${path.headPath}")
    private String headPath;

    /**
     * 登录功能
     * @param mail 邮箱
     * @param password 密码
     * @return 登录成功：token
     *         登录失败：失败原因
     */
    @PostMapping("/login")
    @CrossOrigin
    public Result login(String mail, String password) {
        System.out.println(mail + "," + password);
        User user = loginServer.login(mail, password);
        if(user != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("mail", mail);
            claims.put("ID", user.getID());
            String jwt = JwtUtils.generateJwt(claims);
            log.info("用户:" + user.getMail() + "，ID:" + user.getID() + "登录成功");
            return Result.success(jwt);
        }
        log.info("用户尝试登录失败，邮箱为:" + mail);
        return Result.error("login fail");
    }

    /**
     * 获取本人的角色
     * @return 0：学生
     *         1：老师
     */
    @GetMapping("/getRole")
    public Result getRole(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");

        int res = loginServer.getRole(uID);
        return Result.success(res);
    }

//    @PostMapping("/login")
//    @CrossOrigin
//    public Result login(HttpServletRequest request) {
////        System.out.println(mail + "," + password);
////        List<User> users = loginServer.login(mail, password);
////        if(users.size() != 0) {
////            Map<String, Object> claims = new HashMap<>();
////            claims.put("mail", mail);
////            claims.put("ID", users.get(0).getID());
////            String jwt = JwtUtils.generateJwt(claims);
////            log.info("用户:" + users.get(0).getMail() + "，ID:" + users.get(0).getID() + "登录成功");
////            return Result.success(jwt);
////        }
////        log.info("用户尝试登录失败，邮箱为:" + mail);
////        return Result.error("login fail");
//        Enumeration enu=request.getParameterNames();
//        while(enu.hasMoreElements()){
//            String paraName=(String)enu.nextElement();
//            System.out.println(paraName+": "+request.getParameter(paraName));
//        }
//        System.out.println(request.getRemoteHost());
//        System.out.println(request.getRequestURI());
//        System.out.println(request.getQueryString());
//        return Result.success();
//    }

    @PostMapping("/register")
    @CrossOrigin
    public Result register(String mail, String password, String name) {
        int res = loginServer.register(mail, password, name);
        if(res == -1) {
            return Result.error("邮箱已被注册");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("mail", mail);
        claims.put("ID", res);
        String jwt = JwtUtils.generateJwt(claims);
        log.info("用户:" + mail + "，ID:" + name + "注册成功");
        return Result.success(jwt);

    }

    /**
     * 认证学校与学号
     * majority：专业
     * school：学校
     * introduction：个人简介
     * studentID：学号
     * @return 成功：认证成功信息
     *         失败：失败原因
     */
    @PostMapping("/modify")
    @CrossOrigin
    public Result change(HttpServletRequest request) {
        String school = request.getParameter("school");
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");
        String majority = request.getParameter("majority");
        String introduction = request.getParameter("introduction");
        String studentID = request.getParameter("studentID");
        int res = loginServer.perfect(uID, school, majority, introduction, studentID);
        if(res == -1) { return Result.error("学校学号认证失败"); }
        if(res == -2) { return Result.error("请输入学校与学号"); }
        return Result.success();
    }

    //通过token获取头像url
    @PostMapping("/header")
    @CrossOrigin
    public Result header(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");
        String res = loginServer.getHeader(uID);
        return Result.success(res);
    }

    //通过表单方式获取头像
    @PostMapping("/getHeader")
    @CrossOrigin
    public Result getHeader(int uID) {
        String res = loginServer.getHeader(uID);
        return Result.success(res);
    }

    //获取个人信息
    @PostMapping("/getInfo")
    @CrossOrigin
    public Result getInfo(int uID) {
        return Result.success(loginServer.getInfo(uID));
    }

    @PostMapping("/info")
    @CrossOrigin
    public Result info(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");
        String res = loginServer.getHeader(uID);
        return Result.success(loginServer.getInfo(uID));
    }

    @GetMapping("/isTeacher")
    @CrossOrigin
    public boolean isTeacher(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");
        return loginServer.isTeacher(uID);
    }

    @PostMapping("/uploadHeader")
    public Result uploadHeader(HttpServletRequest request) throws IOException {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");
        //获取文件
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");
        String originalFileName = file.getOriginalFilename();
        String newFileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));
        file.transferTo(new File(headPath + newFileName));
        String filePath = OBSUtils.uploadFile(new File(headPath + newFileName), newFileName);
        loginServer.setHeader(uID, filePath);
        return Result.success();
    }

    @PostMapping("/uploadReadme")
    public Result uploadReadme(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");
        String readme = request.getParameter("readme");
        int i = loginServer.modifyReadme(uID, readme);
        return Result.success();
    }

}
