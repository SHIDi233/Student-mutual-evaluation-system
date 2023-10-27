package com.example.web_test.server.impl;


import com.example.web_test.mapper.*;
import com.example.web_test.pojo.*;
import com.example.web_test.server.WareServer;
import com.example.web_test.utils.JGitUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
public class WareServerA implements WareServer {

    @Autowired
    private WareMapper wareMapper;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WareApprMapper wareApprMapper;

    @Autowired
    private SysApprMapper sysApprMapper;

    @Autowired
    private NotationMapper notationMapper;

    @Autowired
    private LiveApprMapper liveApprMapper;

    @Override
    public void create(int saID) {
        SysApproval sysAppr = sysApprMapper.getSysAppr(saID);
        User user = userMapper.getUser(sysAppr.getSenderID());

        Warehouse ware = createWare(sysAppr.getWName(), sysAppr.getSenderID(), user.getName(), sysAppr.getContent());
        groupMapper.invite(sysAppr.getSenderID(), ware.getWID());
        String content = "您的仓库:" + sysAppr.getWName() + "创建请求已通过，现在您可以通过如下url克隆: git@123.57.181.79:" + ware.getWPath();
        notationMapper.createNote("系统", "sys", content, sysAppr.getSenderID());

    }

    @Override
    public int applyLive(int uID, String wName, String content, String sTime) {
        List<Warehouse> warehouses = wareMapper.list_name(wName);
        liveApprMapper.initiate(uID, warehouses.get(0).getAdminID(), warehouses.get(0).getWID(), content, sTime);
        return 0;
    }

    @Override
    public String getReadme(String wName) {
        List<Warehouse> warehouses = wareMapper.list_name(wName);
        return warehouses.get(0).getWPath() + "/readme.md";
    }

    @Override
    public String getUrl(String wName) {
        List<Warehouse> warehouses = wareMapper.list_name(wName);
        return "git@123.57.181.79:" + warehouses.get(0).getWPath();
    }

    @Override
    public List<Member> getMembers(String wName) {
        List<Warehouse> warehouses = wareMapper.list_name(wName);
        List<Group> members = groupMapper.getMembers(warehouses.get(0).getWID());
        List<Member> memberList = new ArrayList<>();
        for (Group g : members) {
            User user = userMapper.getUser(g.getUID());
            boolean isAdmin = false;
            if(user.getID() == warehouses.get(0).getAdminID()) {
                isAdmin = true;
            }

            Member member = new Member(user.getName(), user.getMail(), isAdmin);
            memberList.add(member);
        }
        return memberList;
    }

    @Override
    public List<String> getBranch(String wName) {
        List<Warehouse> warehouses = wareMapper.list_name(wName);
        return JGitUtils.getBranches(warehouses.get(0).getWPath());
    }

    @Override
    public List<Warehouse> getWarehouses(int uID) {
        List<Warehouse> wareList = new ArrayList<>();
        List<Group> groups = groupMapper.getWares(uID);
        for(Group group : groups) {
            List<Warehouse> warehouse = wareMapper.list(group.getWID());
            wareList.add(warehouse.get(0));
        }
        return wareList;
    }

    @Override
    public Warehouse createWare(String wName, int adminID, String adminName, String describe) {
        //分配仓库
        String rootPath = "/home/gitcnn/warehouse/";
        rootPath += "user_" + adminID + "/";
        rootPath += wName + ".git";
        try {
            Git git = Git.init().setDirectory(new File(rootPath)).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        //将信息添加到数据库中
        Warehouse warehouse = new Warehouse();
        warehouse.setDescribe(describe);
        warehouse.setAdminID(adminID);
        warehouse.setWPath(rootPath);
        warehouse.setWName(wName);
        wareMapper.insertWare(warehouse);
        return warehouse;
    }

    @Override
    public List<Map<String, String>> getFiles(String wName, String path, String branch) {
        List<Warehouse> wareList = wareMapper.list_name(wName);
        String warePath = wareList.get(0).getWPath();
        List<String> fileList = null;
        try {
            fileList = JGitUtils.getFiles(warePath, branch);
        } catch (IOException | GitAPIException e) {
            throw new RuntimeException(e);
        }

        Set<Map<String, String>> fileSet = new HashSet<>();

        String[] pathDirs = path.split("/");
        List<String> pathDir = new ArrayList<>(Arrays.asList(pathDirs));
        if(pathDirs[pathDirs.length - 1].compareTo("..") == 0) {
            pathDir.remove(pathDir.size() - 1);
            pathDir.remove(pathDir.size() - 1);
        }
        StringBuilder filePath = new StringBuilder();
        for(String p : pathDir) {
            filePath.append(p).append("/");
        }
        for(String l : fileList) {
//            if(l.contains("/")) {
//                fileMaps.put(l, "dir");
//            } else {
//                fileMaps.put(l, "file");
//            }
            Map<String, String> fileMaps = new HashMap<>();
            String[] names = l.split("/");
            if(path.compareTo("") == 0) {
                fileMaps.put("fileName", names[0]);
                fileMaps.put("path", "");
                if(names.length == 1) {
                    fileMaps.put("type", "file");
                } else {
                    fileMaps.put("type", "dir");
                }
                fileSet.add(fileMaps);
                continue;
            }
            boolean isOK = true;
            for(int i = 0; i < pathDir.size(); i++) {
                if(i < names.length && names[i].compareTo(pathDir.get(i)) != 0) {
                    isOK = false;
                    break;
                }
            }
            if(isOK) {
                fileMaps.put("fileName", names[pathDir.size()]);
                fileMaps.put("path", filePath.toString());
                if(names.length == pathDir.size() + 1) {
                    fileMaps.put("type", "file");
                } else {
                    fileMaps.put("type", "dir");
                }
                fileSet.add(fileMaps);
            }
        }
        List<Map<String, String>> files = new ArrayList<>(fileSet);
        files.sort((o1, o2) -> {
            if(o1.get("type").compareTo("dir") == 0 && o2.get("type").compareTo("file") == 0) {
                return -1;
            }
            if(o2.get("type").compareTo("dir") == 0 && o1.get("type").compareTo("file") == 0) {
                return 1;
            }
            return o1.get("fileName").compareTo(o2.get("fileName"));
        });
        if(pathDir.size() != 0 && path.compareTo("") != 0) {
            Map<String, String> a = new HashMap<>();
            a.put("fileName", "..");
            a.put("path", filePath.toString());
            a.put("type", "dir");
            files.add(0, a);
        }
        return files;
    }

    @Override
    public int invite(int sID, String mail, String wName, String content) {
        //判断被邀请人是否存在
        List<User> users = userMapper.getUsers(mail);
        if(users.size() == 0) {
            return -1;//不存在
        }
        int iID = users.get(0).getID();

        //判断邀请人是否为仓库管理员
        List<Warehouse> wares = wareMapper.isAdmin(wName, sID);
        List<Warehouse> warehouses = wareMapper.list_name(wName);
        if(wares.size() == 0) {
            //不是仓库管理员，发起审批
            if(!initiateApproval(sID, warehouses.get(0).getAdminID(), wName, 0, content, iID)) {
                return 1;//无法重复发送邀请
            }
            return 2;//成功发起审批
        }

        //是管理员，直接将被邀请人拉入
        inviteInto(sID, iID, wName);

        //通知被邀请人
        User admin = userMapper.getUser(sID);
        String noteContent = "你被" + admin.getName() + "拉入了仓库：" + wName;
        notationMapper.createNote(users.get(0).getName(), "sys", noteContent, iID);
        return 0;
    }

    @Override
    public void approval(int waID) {
        WareApproval approval = wareApprMapper.getAppr_waID(waID);
        groupMapper.invite(approval.getInviterID(), approval.getWID());
    }

    //发起审批
    public boolean initiateApproval(int sID, int rID, String wName, int type, String content, int iID) {
//        List<Warehouse> wareID = wareMapper.getWareID(sID, wName);
//        int wID = wareID.get(0).getWID();
        int wID = wareMapper.getWareID(wName);
        List<WareApproval> wareApprovals = wareApprMapper.selects(sID, rID, wID);
        if(wareApprovals.size() != 0 && wareApprovals.get(0).getGrant() == 0) {
            return false;
        }
        wareApprMapper.initiate(sID, rID, wID, type, content, iID);
        return true;
    }

    public void inviteInto(int sID, int rID, String wName) {
//        List<Warehouse> wareID = wareMapper.getWareID(sID, wName);
//        int wID = wareID.get(0).getWID();
        int wID = wareMapper.getWareID(wName);
        groupMapper.invite(rID, wID);
    }

    @Override
    public int applyWare(int uID, String wName, String content) {
        //判断仓库是否重名
        Integer wareID = wareMapper.getWareID(wName);
        if(wareID != null) {
            //重名
            return -1;
        }
        sysApprMapper.sysAppr(uID, wName, content, 0);
        return 0;
    }

    public void createNote(String senderName, String scope, int receiverID, String content) {


    }
}
