package com.example.web_test.server;

import com.example.web_test.pojo.Post;

import java.util.List;
import java.util.Map;

public interface PostServer {

    void AiReply(int pID, String content);

    int createPost(int uID, int cID, String content, String pName, boolean teacherOnly);

    List<Map<String, Object>> getPosts(int uID, int cID);

    int reply(int uID, int pID, String content);

    Map<String, Object> getReply(int uID, int pID);

    Post getPost(int pID);
}
