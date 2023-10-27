package com.example.web_test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiveApproval {

    private int laID;
    private int senderID;
    private int receiverID;
    private int grant;
    private int wID;
    private boolean readStat;
    private String content;
    private LocalDateTime createTime;
    private LocalDate startTime;

}
