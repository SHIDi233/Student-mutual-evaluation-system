package com.example.web_test.utils;

import com.example.web_test.pojo.Post;
import com.example.web_test.server.PostServer;
import com.example.web_test.server.impl.PostServerA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

//@Component
public class GlmUtils implements Runnable{
    private ApplicationContext applicationContext = SpringUtils.getApplicationContext();
    private PostServer postServer = applicationContext.getBean(PostServerA.class);

    public static List<Integer> waitList = new ArrayList<>();

    public static String getReply(String ask) throws IOException {
        // 调用CMD命令
        Process process = Runtime.getRuntime().exec("cmd.exe /c main.exe", null, new File("D:\\BaiduNetdiskDownload\\glm\\"));

        OutputStream outputStream = process.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

        writer.write(ask + "\r\n");
        writer.flush();
        writer.flush();
        writer.flush();
        writer.flush();
//        System.out.println("写入成功：" + ask + "：");

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)); // 设置编码为GBK
        String line;
        while ((line = reader.readLine()) != null) {
//            System.out.println(line);
            if(line.startsWith("ChatGLM3 > ")) { return line.substring(11); }
        }
        return null;
    }

    @Override
    public void run() {
        while (true) {
            if(GlmUtils.waitList.size() != 0) {
                System.out.println("正在处理" + GlmUtils.waitList.get(0) + "号贴子");
                int pID = GlmUtils.waitList.get(0);
                GlmUtils.waitList.remove((int) 0);
                Post post = postServer.getPost(pID);
                System.out.println("glm获取内容成功");
                if (post == null) { continue; }
                String content = post.getContent();
                try {
                    content = GlmUtils.getReply(content);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                postServer.AiReply(pID, content);

            } else {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}