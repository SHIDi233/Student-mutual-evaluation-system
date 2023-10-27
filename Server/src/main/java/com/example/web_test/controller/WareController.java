package com.example.web_test.controller;

import com.example.web_test.pojo.Member;
import com.example.web_test.pojo.Result;
import com.example.web_test.pojo.Warehouse;
import com.example.web_test.server.WareServer;
import com.example.web_test.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class WareController {

    @Autowired
    private WareServer wareServer;

    @RequestMapping("/Wares")
    @CrossOrigin
    public Result listWares(HttpServletRequest request) {
        int uID;
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        uID = (int) claims.get("ID");
        List<Warehouse> warehouses = wareServer.getWarehouses(uID);
        log.info("访问仓库列表，ID为" + uID);
        return Result.success(warehouses);
    }

    @PostMapping("/create")
    public Result createWare(int userID, String wName, String description) {
        return null;
    }

    @GetMapping("/getFiles")
    public Result getWareFiles(HttpServletRequest request) {
        String wName = request.getParameter("wName");
//        int uID;
//        String jwt = request.getHeader("token");
//        Claims claims = JwtUtils.parseJWT(jwt);
//        uID = (int) claims.get("ID");
        String path = request.getParameter("path");
        String branch = request.getParameter("branch");
        List<Map<String, String>> fileMaps = wareServer.getFiles(wName, path, branch);
        return Result.success(fileMaps);
    }

    //申请创建仓库
    @PostMapping("/applyWare")
    public Result applyWare(HttpServletRequest request) {

        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        int uID = (int) claims.get("ID");
        String wName = request.getParameter("wName");
        String description = request.getParameter("description");

        int i = wareServer.applyWare(uID, wName, description);
        log.info("ID为" + uID + "的用户申请创建仓库:" + wName);
        if(i == -1) {
            log.info("仓库名重复");
            return Result.error("Name has been used");
        }
        log.info("申请成功");
        return Result.success();
    }

    @PostMapping("/invite")
//    public Result invite(int sID, String mail, String wName, String content) {
    public Result invite(HttpServletRequest request) {
        String mail = request.getParameter("mail");
        String wName = request.getParameter("wName");
        String content = request.getParameter("content");
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        int sID = (int) claims.get("ID");
        log.info("id为" + sID + "的用户请求将邮箱为" + mail + "的用户拉入仓库:" + wName);
        int feedback = wareServer.invite(sID, mail, wName, content);
        if(feedback == -1) {
            log.info("拉入请求失败，被邀请人未找到");
            return Result.error("Receiver not found");
        } else if (feedback == 0) {
            log.info("邀请人为管理员，拉入成功");
            return Result.success("Invite success!");
        } else if (feedback == 2){
            log.info("邀请人请求审批");
            return Result.success("Request sent");
        } else if (feedback == 1) {
            return Result.error("Already invited!");
        }
        return Result.error("");
    }

    @PostMapping("/applyLive")
    public Result applyLive(HttpServletRequest request) {
        String wName = request.getParameter("wName");
        String content = request.getParameter("content");
        String sTime = request.getParameter("sTime");

        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        int sID = (int) claims.get("ID");
        log.info("id为" + sID + "的用户请求在仓库" + wName + "发起会议直播");
        wareServer.applyLive(sID, wName, content, sTime);
        return Result.success();
    }

    @GetMapping("/readme")
    public ResponseEntity<Resource> readMe(String wName) throws FileNotFoundException {
        File file = new File(wareServer.getReadme(wName));
        if(!file.exists()) { return null; }
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/getUrl")
    public Result getUrl(String wName) {
        String url = wareServer.getUrl(wName);
        return Result.success(url);
    }

    @GetMapping("/getMembers")
    public Result getMembers(String wName) {
        List<Member> members = wareServer.getMembers(wName);
        return Result.success(members);
    }

    @GetMapping("/getBranch")
    public Result getBranch(String wName) {
        return Result.success(wareServer.getBranch(wName));
    }


}
