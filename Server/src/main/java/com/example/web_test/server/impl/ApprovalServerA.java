package com.example.web_test.server.impl;

import com.example.web_test.mapper.*;
import com.example.web_test.pojo.*;
import com.example.web_test.server.ApprovalServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class ApprovalServerA implements ApprovalServer {

    @Autowired
    private WareApprMapper wareApprMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WareMapper wareMapper;

    @Autowired
    private SysApprMapper sysApprMapper;

    @Autowired
    private NotationMapper notationMapper;

    @Autowired
    private LiveApprMapper liveApprMapper;

    @Autowired
    private ClassMapper classMapper;


    @Override
    public List<Notation> getAppr(int uID) {
//        List<WareApproval> wareApprovals = wareApprMapper.getAppr_rID(uID);
//        List<Notation> notations = new ArrayList<>();
//        for (WareApproval wareApproval : wareApprovals) {
//            User user = userMapper.getUser(wareApproval.getSenderID());
//            List<Warehouse> warehouses = wareMapper.list(wareApproval.getWID());
//            String type;
//            if(wareApproval.getGrant() == 1) {
//                type = "WA-YES";
//            } else if (wareApproval.getGrant() == -1) {
//                type = "WA_NO";
//            } else {
//                type = "WA";
//            }
//            Notation notation = new Notation(wareApproval.getWaID(), type,
//                    wareApproval.isReadStat(), user.getName(), warehouses.get(0).getWName(),
//                    wareApproval.getContent(), wareApproval.getCreateTime(), uID);
//            notations.add(notation);
//        }
//
//        List<LiveApproval> liveApprovals = liveApprMapper.select();
//        for(LiveApproval liveApproval : liveApprovals) {
//            User user = userMapper.getUser(liveApproval.getSenderID());
//            List<Warehouse> warehouses = wareMapper.list(liveApproval.getWID());
//            String type;
//            if(liveApproval.getGrant() == 1) {
//                type = "LA-YES";
//            } else if (liveApproval.getGrant() == -1) {
//                type = "LA_NO";
//            } else {
//                type = "LA";
//            }
//            Notation notation = new Notation(liveApproval.getLaID(), type,
//                    liveApproval.isReadStat(), user.getName(), warehouses.get(0).getWName(),
//                    liveApproval.getContent(), liveApproval.getCreateTime(), uID);
//            notations.add(notation);
//        }
//
//        //判断用户身份
//        User u = userMapper.getUser(uID);
//        if(u.getRole() == 1) {
//            //是管理员
//            List<SysApproval> sysApprovals = sysApprMapper.selects();
//            for(SysApproval sysApproval : sysApprovals) {
//                User user = userMapper.getUser(sysApproval.getSenderID());
//                String type;
//                if(sysApproval.getGrant() == 1) {
//                    type = "SA-YES";
//                } else if (sysApproval.getGrant() == -1) {
//                    type = "SA_NO";
//                } else {
//                    type = "SA";
//                }
//                Notation notation = new Notation(sysApproval.getSaID(), type, sysApproval.isReadStat(),
//                        user.getName(), sysApproval.getWName(), sysApproval.getContent(), sysApproval.getCTime(), uID);
//                notations.add(notation);
//            }
//        }
//        List<Notation> notationList = notationMapper.select();
//        for(Notation n : notationList) {
//            if(n.getScope().compareTo("sys") == 0) {
//                notations.add(n);
//            }
//        }
//        //根据时间排序
//        notations.sort((n1, n2) -> n2.getSendTime().compareTo(n1.getSendTime()));
        return null;
    }

    @Override
    public List<Map<String, Object>> getNotations(int uID) {
        List<Notation> notes = notationMapper.getNote(uID);
        List<Map<String, Object>> res = new ArrayList<>();
        for(Notation n : notes) {
            Map<String, Object> m = new HashMap<>();
            m.put("sender", n.getSenderName());
            m.put("scope", n.getScope());
            m.put("content", n.getContent());
            m.put("type", n.getType());
            m.put("time", n.getSendTime().format(DateTimeFormatter.ISO_DATE_TIME));
            m.put("isRead", n.isRead());
            m.put("noteID", n.getID());
            res.add(m);
        }
        return res;
    }

    @Override
    public boolean wareAppr(int waID, int grant) {
        wareApprMapper.appr(waID, grant);
        WareApproval wareApproval = wareApprMapper.getAppr_waID(waID);
        User inviter = userMapper.getUser(wareApproval.getInviterID());
        User receiver = userMapper.getUser(wareApproval.getReceiverID());
        User sender = userMapper.getUser(wareApproval.getSenderID());
        List<Warehouse> warehouses = wareMapper.list(wareApproval.getWID());

        String content;

        if(grant == 1) {
            content = "您对" + inviter.getName() + "的邀请已被同意";
        } else {
            content = "您对" + inviter.getName() + "的邀请已被拒绝";
        }

        //发送通知
        notationMapper.createNote(receiver.getName(), "sys", content, sender.getID());
        //发送仓库通知
        if(grant == 1) {
            String content2 = inviter.getName() + "被成员 " + sender.getName() + " 拉入了仓库";
            notationMapper.createNote(warehouses.get(0).getWName(), warehouses.get(0).getWName(), content2, sender.getID());
        }
        return true;
    }

    @Override
    public boolean sysAppr(int saID, int grant) {
        sysApprMapper.appr(saID, grant);
        return true;
    }

    @Override
    public List<Notation> getWareNotations(String wName) {
        List<Notation> wareNotations = notationMapper.getWareNote(wName);
        wareNotations.sort((n1, n2) -> n2.getSendTime().compareTo(n1.getSendTime()));

        return wareNotations;
    }

    @Override
    public boolean liveAppr(int laID, int grant) {
        liveApprMapper.appr(laID, grant);
        LiveApproval liveApproval = liveApprMapper.getByLaID(laID);
        User sender = userMapper.getUser(liveApproval.getSenderID());
        User receiver = userMapper.getUser(liveApproval.getReceiverID());
        List<Warehouse> warehouses = wareMapper.list(liveApproval.getWID());

        String content;
        if(grant == 1) {
            content = "您在仓库" + warehouses.get(0).getWName() + "中申请的会议已通过，会议时间：" + liveApproval.getStartTime();
        } else {
            content = "您在仓库" + warehouses.get(0).getWName() + "中申请的会议被拒绝 ，会议时间：" + liveApproval.getStartTime();
        }
        //发送通知
        notationMapper.createNote(receiver.getName(), "sys", content, sender.getID());
        //发送仓库通知
        if(grant == 1) {
            String content2 = "成员 " + sender.getName() + " 申请在时间 " + liveApproval.getStartTime() + " 开启会议直播";
            notationMapper.createNote(warehouses.get(0).getWName(), warehouses.get(0).getWName(), content2, sender.getID());
        }
        return true;
    }

    @Override
    public int approve(int uID, int nID, String type, int grant) {
        //Todo: 判断权限
        //修改数据库
        String newType;
        if(grant == 1) {
            newType = type + "-yes";
        } else {
            newType = type + "-no";
        }
        Notation note = notationMapper.getNotation(nID);
        notationMapper.updateNotation(nID, type, newType);
        int aID = note.getApproveID();
        classMapper.updateApproval(aID, grant);
        //加入班级
        ClassApproval classApproval = classMapper.getClassApproval(aID);
        if(grant == 1) {
            classMapper.addMember(classApproval.getClassID(), classApproval.getSenderID(), 0);
            classMapper.updateClassNum(classApproval.getClassID());
        }
        //向申请者发送公告
        Notation notation = new Notation();
        notation.setReceiverID(classApproval.getSenderID());
        notation.setType("NT");
        notation.setScope("系统");
        String className = classMapper.getClasses(classApproval.getClassID()).getClassName();
        notation.setSenderName(className);
        String content;
        if(grant == 1) {
            content = "您加入班级:" + className + " 的申请已通过。";
        } else {
            content = "您加入班级:" + className + " 的申请被拒绝。";
        }
        notation.setContent(content);
        notationMapper.createNote1(notation);
        return 0;
    }

    @Override
    public Map<String, Object> getNotation(int uID, int nID) {
        List<Notation> note = notationMapper.getNote(uID);
        if(note.size() == 0) { return null; }
        Notation notation = note.get(0);
        Map<String, Object> m = new HashMap<>();
        m.put("sender", notation.getSenderName());
        m.put("scope", notation.getScope());
        m.put("content", notation.getContent());
        m.put("type", notation.getType());
        m.put("time", notation.getSendTime());
        m.put("isRead", notation.isRead());
        m.put("noteID", notation.getID());
        return m;
    }

    @Override
    public int getUnread(int uID) {
        return notationMapper.getUnread(uID);
    }

    @Override
    public String readNotation(int uID, int nID) {
        Notation notation = notationMapper.readNotation(uID, nID);
        if(notation == null) { return ""; }
        notationMapper.setRead(nID);
        return notation.getContent();
    }
}
