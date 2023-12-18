package com.example.web_test;

import com.example.web_test.utils.GlmUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@ServletComponentScan
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@EnableAsync
public class WebTestApplication {
    public static void main(String[] args) {

        SpringApplication.run(WebTestApplication.class, args);

        //开启 glm 线程
        System.out.println("开启glm线程");
        GlmUtils glmUtils = new GlmUtils();
        Thread glmThread = new Thread(glmUtils, "glmThread");
        glmThread.start();
    }

}
