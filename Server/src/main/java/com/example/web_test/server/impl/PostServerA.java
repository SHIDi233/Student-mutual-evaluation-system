package com.example.web_test.server.impl;

import com.example.web_test.mapper.ClassMapper;
import com.example.web_test.mapper.PostMapper;
import com.example.web_test.mapper.UserMapper;
import com.example.web_test.pojo.Post;
import com.example.web_test.pojo.Reply;
import com.example.web_test.pojo.User;
import com.example.web_test.server.PostServer;
import com.example.web_test.utils.GlmUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PostServerA implements PostServer {

    @Autowired
    UserMapper userMapper;

    @Autowired
    PostMapper postMapper;

    @Autowired
    ClassMapper classMapper;

    @Override
    public void AiReply(int pID, String content) {
        //获取楼数
        int floor = postMapper.getFloor(pID);
        postMapper.addFloor(pID); //楼数加一

        //回复
        postMapper.reply(0, pID, content, floor + 1);
    }


    @Override
    public int createPost(int uID, int cID, String content, String pName, boolean teacherOnly) {
        //todo: 判断是否有权限

        Post post = new Post();
        post.setCID(cID);
        post.setUID(uID);
        post.setContent(content);
        post.setPName(pName);
        post.setTeacherOnly(teacherOnly);
        postMapper.createPost(post);

        //AI助教自动回复
        GlmUtils.waitList.add(post.getPID());

        return 0;
    }

    @Override
    public List<Map<String, Object>> getPosts(int uID, int cID) {
        List<Map<String, Object>> res = new ArrayList<>();
        //todo: 判断是否有权限

        List<Post> posts = postMapper.getPosts(cID);
        for (Post post : posts) {
            User user = userMapper.getUser(post.getUID());
            if(user.getRole() != 1 && post.isTeacherOnly()) { continue; } //判断是否可见
            Map<String, Object> m = new HashMap<>();
            m.put("pID", post.getPID());
            m.put("uName", user.getName());
            m.put("head", user.getHead());
            m.put("pName", post.getPName());
            String cTime = post.getCTime().format(DateTimeFormatter.ofPattern("MM-dd HH:mm"));
            if(post.getCTime().toLocalDate().equals(LocalDate.now())) {
                cTime = post.getCTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            } else if(post.getCTime().toLocalDate().equals(LocalDate.now().minusDays(1))) {
                cTime = post.getCTime().format(DateTimeFormatter.ofPattern("昨天 HH:mm"));
            }
            m.put("cTime", cTime);
            m.put("teacherOnly", post.isTeacherOnly());
            m.put("profile", post.getContent().length()>20 ? post.getContent().substring(0, 20) + "..." : post.getContent());
            res.add(m);
        }
        return res;
    }

    @Override
    public int reply(int uID, int pID, String content) {
        //todo: 判断是否有权限

        //获取楼数
        int floor = postMapper.getFloor(pID);
        postMapper.addFloor(pID); //楼数加一

        //回复
        postMapper.reply(uID, pID, content, floor + 1);
        return 0;
    }

    @Override
    public Map<String, Object> getReply(int uID, int pID) {
        Map<String, Object> res = new HashMap<>();
        //todo: 判断是否有权限

        //获取帖子相关信息
        Post post = postMapper.getPost(pID);
        User user = userMapper.getUser(post.getUID());
        res.put("uID", user.getID());
        res.put("head", user.getHead());
        res.put("uName", user.getName());
        res.put("pName", post.getPName());
        res.put("content", post.getContent());
        res.put("cTime", post.getCTime());
        res.put("role", user.getRole() == 1 ? "老师" : "学生");

        //获取回复
        List<Reply> replies = postMapper.getReplies(pID);
        List<Map<String, Object>> rep = new ArrayList<>();
        for(Reply reply : replies) {
            Map<String, Object> m = new HashMap<>();
            User user1 = userMapper.getUser(reply.getUID());
            m.put("uID", user1.getID());
            m.put("uName", user1.getName());
            m.put("head", user1.getHead());
            m.put("rID", reply.getRID());
            m.put("content", reply.getContent());
            m.put("floor", reply.getFloor());
            m.put("sendTime", reply.getSendTime());
            res.put("role", user1.getRole() == 1 ? "老师" : "学生");
            rep.add(m);
        }
        res.put("reply", rep);
        return res;
    }

    @Override
    public Post getPost(int pID) {
        return postMapper.getPost(pID);
    }
}
