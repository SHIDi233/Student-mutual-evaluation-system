package com.example.web_test.server;

import com.example.web_test.pojo.Notation;

import java.util.List;
import java.util.Map;

public interface ApprovalServer {

    public List<Notation> getAppr(int uID);

    public boolean wareAppr(int waID, int grant);

    public boolean sysAppr(int saID, int grant);

    public List<Notation> getWareNotations(String wName);

    public boolean liveAppr(int laID, int grant);

    List<Map<String, Object>> getNotations(int uID);

    int approve(int uID, int aID, String type, int grant);

    Map<String, Object> getNotation(int uID, int nID);

    int getUnread(int uID);

    String readNotation(int uID, int nID);
}
