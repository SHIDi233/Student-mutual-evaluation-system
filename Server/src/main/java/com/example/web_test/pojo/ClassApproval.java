package com.example.web_test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassApproval {
    private int caID;
    private int senderID;
    private int grant;
    private int type;
    private String content;
    private LocalDateTime cTime;
    private int classID;
}
