package com.example.web_test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reply {

    private int uID;
    private int rID;
    private int pID;
    private String content;
    private int floor;
    private LocalDateTime sendTime;

}
