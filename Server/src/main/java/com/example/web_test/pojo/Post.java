package com.example.web_test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    private int pID;
    private String pName;
    private int cID;
    private LocalDateTime cTime;
    private int uID;
    private String content;
    private boolean teacherOnly;
    private int numOfReply;
    private boolean isTop;

}
