package com.example.web_test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Homework {

    private int hwID;
    private int classID;
    private LocalDateTime cTime;
    private LocalDateTime ddl;
    private String content;
    private boolean isPublish;
    private String hwName;
    private int creatorID;
    private boolean isStartEva;
    private int score;
    private int cID;

}
