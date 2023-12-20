package com.example.web_test.utils;

import com.example.web_test.pojo.Post;
import com.example.web_test.server.PostServer;
import com.example.web_test.server.impl.PostServerA;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

//@Component
public class AnalyseUtils{

    public static void main(String[] args) throws IOException {
        String reply = getReply("离散数学：100,90,80,70");
        System.out.println(reply);
    }

    public static String getReply(String ask) throws IOException {
        // 调用CMD命令
        Process process = Runtime.getRuntime().exec("cmd.exe /c analyse.exe", null, new File("D:\\BaiduNetdiskDownload\\glm\\"));

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
        String res = "";
//        while ((line = reader.readLine()) != null) {
//            System.out.println(line);
//            if(line.startsWith("ChatGLM3 > ")) {
//                res = res + line.substring(11);
//                while ((line = reader.readLine()) != null) {
//                    System.out.println(line);
//                    res = res + "\n" + line;
//                }
//                break;
//            }
//        }
        char[] chars = new char[1024];
        int len = 0;
        while ((len = reader.read(chars)) != 0) {
            System.out.print(String.valueOf(chars, 0, len));
        }
        return res;
    }

    public static void getReply(String ask, ServletOutputStream out) throws IOException {
        // 调用CMD命令
        Process process = Runtime.getRuntime().exec("cmd.exe /c analyse.exe", null, new File("D:\\BaiduNetdiskDownload\\glm\\"));

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
        String res = "";
//        while ((line = reader.readLine()) != null) {
//            System.out.println(line);
//            if(line.startsWith("ChatGLM3 > ")) {
//                res = res + line.substring(11);
//                while ((line = reader.readLine()) != null) {
//                    System.out.println(line);
//                    res = res + "\n" + line;
//                }
//                break;
//            }
//        }
        char[] chars = new char[1024];
        int len = 0;
        try {
            while ((len = reader.read(chars)) != 0) {
                System.out.print(String.valueOf(chars, 0, len));
                out.write(String.valueOf(chars, 0, len).getBytes());
                out.flush();
            }
        } catch (StringIndexOutOfBoundsException e) {
//            return;
        } finally {
            out.close();
            reader.close();
        }
//        return res;
    }

}