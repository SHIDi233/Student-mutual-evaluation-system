package com.example.web_test.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

    //将以#开头的行都删去
    public static String deleteHead(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder result = new StringBuilder();
        String[] lines = input.split("\n");

        for (String line : lines) {
            if (!line.trim().startsWith("#")) {
                result.append(line).append("\n");
            }
        }

        // Remove the trailing newline character if it exists
        if (result.length() > 0 && result.charAt(result.length() - 1) == '\n') {
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }

    //获取代码块
    public static List<String> findCode(String input) {
        List<String> result = new ArrayList<>();

        // Define the regex pattern for matching content inside triple backticks
        Pattern pattern = Pattern.compile("```\\n(.*?)\\n```", Pattern.DOTALL);

        // Use a Matcher to find all matches in the input string
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            // Group 1 contains the content inside the triple backticks
            result.add(matcher.group(1));
        }

        return result;
    }

    public static List<String> extractImageLinks(String markdownText) {
        List<String> imageLinks = new ArrayList<>();

        // Define the regex pattern for matching image links in markdown
        Pattern pattern = Pattern.compile("!\\[.*?\\]\\((.*?)\\)");

        // Use a Matcher to find all matches in the Markdown text
        Matcher matcher = pattern.matcher(markdownText);

        while (matcher.find()) {
            // Group 1 contains the image link
            imageLinks.add(matcher.group(1));
        }

        return imageLinks;
    }

    public static List<String> downloadImages(List<String> imageLinks, String targetFolder) {
        List<String> newPath = new ArrayList<>();
        try {
            for (String imageLink : imageLinks) {
                URL imageUrl = new URL(imageLink);

                // Get the file name from the URL
                String fileName = imageLink.substring(imageLink.lastIndexOf('/') + 1);

                // Create a Path for the target file in the specified folder
                Path targetPath = Paths.get(targetFolder, fileName);


                // Download the image and save it to the target file
                Files.copy(imageUrl.openStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                newPath.add(targetPath.toString());

                System.out.println("Downloaded: " + fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newPath;
    }

    public static void deleteImages(List<String> imageUrls) {
        for (String imageUrl : imageUrls) {
            try {
                // 创建File对象
                File imageFile = new File(imageUrl);

                // 检查文件是否存在，并删除
                if (imageFile.exists()) {
                    if (imageFile.delete()) {
                        System.out.println("Deleted: " + imageUrl);
                    } else {
                        System.err.println("Failed to delete: " + imageUrl);
                    }
                } else {
                    System.out.println("File does not exist: " + imageUrl);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String extractMainContent(String markdownText) {
        // Remove images
        markdownText = markdownText.replaceAll("!\\[.*?\\]\\(.*?\\)", "");

        // Remove code blocks
        markdownText = markdownText.replaceAll("```[\\s\\S]*?```", "");

        // Remove other markdown elements (e.g., links, emphasis, etc.)
        markdownText = markdownText.replaceAll("\\[.*?\\]\\(.*?\\)", "");
        markdownText = markdownText.replaceAll("\\*\\*.*?\\*\\*", "");
        markdownText = markdownText.replaceAll("\\*.*?\\*", "");
        markdownText = markdownText.replaceAll("_.*?_", "");

        // Remove HTML tags
        markdownText = markdownText.replaceAll("<[^>]+>", "");

        // Trim extra whitespace
        markdownText = markdownText.trim();

        return markdownText;
    }

//    public static void main(String[] args) {
//        // Example usage:
//        String inputString = "Some text before\n```\nContent1\n```\nText in between\n```\nContent2\n```\nSome text after";
//        String[] extractedContent = findCode(inputString);
//        System.out.println(inputString);
//        // Print the extracted content
//        for (String content : extractedContent) {
//            System.out.println("Extracted Content: " + content);
//        }
//    }

}
