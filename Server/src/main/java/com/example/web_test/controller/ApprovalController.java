package com.example.web_test.controller;

import com.example.web_test.pojo.Notation;
import com.example.web_test.pojo.Result;
import com.example.web_test.server.ApprovalServer;
import com.example.web_test.server.WareServer;
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
public class ApprovalController {

    @Autowired
    private ApprovalServer approvalServer;

    @Autowired
    private WareServer wareServer;

    @PostMapping("/WareAppr")
    public Result wareAppr(int waID, int grant) {
        if(approvalServer.wareAppr(waID, grant)) {
            if(grant == 1) {
                wareServer.approval(waID);
            }
            return Result.success();
        } else {
            return Result.error("");
        }
    }

    @PostMapping("/ClassAppr")
    public Result classAppr(int ID, int grant) {
        if(approvalServer.wareAppr(ID, grant)) {
            if(grant == 1) {
                wareServer.approval(ID);
            }
            return Result.success();
        } else {
            return Result.error("");
        }
    }

    @PostMapping("/LiveAppr")
    public Result liveAppr(int laID, int grant) {
        approvalServer.liveAppr(laID, grant);
        return Result.success();
    }

    @PostMapping("/SysAppr")
    public Result sysAppr(int saID, int grant) {
        log.info("saID:" + saID + "审批结果为" + grant);
        if(approvalServer.sysAppr(saID, grant)) {
            if(grant == 1) {
                //创建仓库
                wareServer.create(saID);
            }
            return Result.success();
        } else {
            return Result.error("");
        }
    }

    @GetMapping("/Notations")
    public Result getNotations(HttpServletRequest request) {
        String jwt = request.getHeader("token");
//        System.out.println(jwt);
        Claims claims = JwtUtils.parseJWT(jwt);
        int uID = (int) claims.get("ID");
        List<Map<String, Object>> notations = approvalServer.getNotations(uID);
        log.info("ID为" + uID + "的用户访问公告");
        return Result.success(notations);
    }

    @GetMapping("/WareNotations")
    public Result getWareNotations(String wName) {
        List<Notation> wareNotations = approvalServer.getWareNotations(wName);
        log.info("仓库 " + wName + " 的公告被访问");
        return Result.success(wareNotations);
    }

    @PostMapping("classApprove")
    public Result approve(HttpServletRequest request) {
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        int uID = (int) claims.get("ID");
        int aID = Integer.parseInt(request.getParameter("noteID"));
        String type = request.getParameter("type");
        int grant = Integer.parseInt(request.getParameter("grant"));
        int i = approvalServer.approve(uID, aID, type, grant);
        if(i != 0) {
            return Result.error("请求失败");
        }
        return Result.success();
    }

    @PostMapping("/getNotation")
    public Result getNotation(HttpServletRequest request) {
        String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJWT(jwt);
        int uID = (int) claims.get("ID");
        int nID = Integer.parseInt(request.getParameter("noteID"));
        Map<String, Object> res = approvalServer.getNotation(uID, nID);
        return Result.success(res);
    }

}
