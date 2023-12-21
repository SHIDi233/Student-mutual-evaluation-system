package com.example.web_test.utils.DuplicateCheck;

import com.example.web_test.utils.TextUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DuplicateCheckUtils {
    public static double getRate(String str0, String str1) {

        // 由字符串得出对应的 simHash值
        String simHash0 = SimHash.getSimHash(str0);
        String simHash1 = SimHash.getSimHash(str1);

        // 由 simHash值求出相似度
        return Hamming.getSimilarity(simHash0, simHash1);
    }

    //正则表达式匹配
    public static int extractPercentage(String output) {
        Pattern pattern = Pattern.compile("\\b(\\d{1,2}|100)\\s*%\\s*\\b");
        Matcher matcher = pattern.matcher(output);

        if (matcher.find()) {
            String percentageStr = matcher.group(1);
            return Integer.parseInt(percentageStr);
        }

        return -1;
    }

    //代码查重
    public static int checkCode(String code1, String code2) {
        //将代码存储到文件中
//        BufferedWriter bw = new BufferedWriter(new FileWriter(""));
        String outputFolder = "D:\\vue";
        StringBuilder output = new StringBuilder();
        try {
            // Create a folder if it doesn't exist
            Path folderPath = Paths.get(outputFolder);
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            // Write code1 to file1
            Path file1 = folderPath.resolve("code1.cpp");
            Files.write(file1, code1.getBytes());

            // Write code2 to file2
            Path file2 = folderPath.resolve("code2.cpp");
            Files.write(file2, code2.getBytes());

            // Call external program sim_c++.exe with parameters -o, file1, and file2
            ProcessBuilder processBuilder = new ProcessBuilder("D:\\vue\\sim_c++.exe", "-p", file1.toString(), file2.toString());
//            processBuilder.directory(new File("D:\\vue\\sim_c++.exe"));
            processBuilder.redirectErrorStream(true);

            // Start the external program
            Process process = processBuilder.start();

            // Wait for the process to finish
            int exitCode = process.waitFor();

            // Print the output of the external program
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
//                System.out.println(line);
            }

//            System.out.println("External program exited with code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return extractPercentage(output.toString());
    }

    public static boolean checkImg(String img1, String img2) {
        StringBuilder result = new StringBuilder();
        try {
//            ProcessBuilder processBuilder = new ProcessBuilder("C:\\Users\\HP\\Downloads\\out\\imgHashCompare.exe", img1, img2);
//            processBuilder.directory(new File("C:\\Users\\HP\\Downloads\\out\\"));
//            processBuilder.redirectErrorStream(true);
//
//            // Start the external program
//            Process process = processBuilder.start();
            Process process = Runtime.getRuntime().exec("cmd.exe /c imgHashCompare.exe " + img1 + " " + img2, null, new File("C:\\Users\\HP\\Downloads\\out\\"));

            // Wait for the process to finish
            int exitCode = process.waitFor();

            // Print the output of the external program
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
                System.out.println(line);
            }

            System.out.println("External program exited with code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return result.toString().contains("1");
    }

//    public static void main(String[] args) {
//        System.out.println(checkImg("C:\\Users\\HP\\Downloads\\out\\3.png", "C:\\Users\\HP\\Downloads\\out\\1.jpg"));
//    }


}
