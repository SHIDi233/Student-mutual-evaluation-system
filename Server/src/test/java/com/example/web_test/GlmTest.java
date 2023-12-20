package com.example.web_test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class GlmTest {

    public static void main(String[] args) throws IOException, InterruptedException {

        Scanner scanner = new Scanner(System.in);
        String cmd = null;
        // 调用CMD命令
        Process process = Runtime.getRuntime().exec("cmd.exe /c main.exe", null, new File("D:\\BaiduNetdiskDownload\\glm\\"));
//        Process process = Runtime.getRuntime().exec("cmd");
        //开启读线程
//        ReadThread read = new ReadThread(process.getInputStream());
//        Thread readThread = new Thread(read, "readThread");
//        readThread.start();
//
//        ReadThread error = new ReadThread(process.getInputStream());
//        Thread errorThread = new Thread(error, "errorThread");
//        errorThread.start();

        InputStreamReader isr = new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8);
        OutputStream outputStream = process.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        while (true) {
            System.out.println("请写入命令：");
            cmd = scanner.nextLine();
            if (cmd.equals("quit")) {
                break;
            }
            writer.write(cmd + "\r\n");
            writer.flush();
            writer.flush();
            writer.flush();
            writer.flush();
            System.out.println("写入成功：" + cmd + "：");

//            //输出
//            System.out.println("输出：");
//            char[] out = new char[10240];
//            int len;
//            len = isr.read(out);
//            System.out.println(String.valueOf(out, 0, len));
//            System.out.println(len);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)); // 设置编码为GBK
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

//            Thread.sleep(60000);
        }
    }

//    static class WriteThread implements Runnable {
//        OutputStream os;
//        public WriteThread(OutputStream os){
//            this.os = os;
//        }
//
//        @Override
//        public void run() {
//
//        }
//    }
//
//    static class ReadThread implements Runnable {
//        InputStream is;
//        public ReadThread(InputStream is){
//            this.is = is;
//        }
//
//        @Override
//        public void run() {
//            try {
//
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "GBK")); // 设置编码为GBK
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    System.out.println(line);
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
}


