package com.example.web_test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WareApproval {

    private int waID;
    private int senderID;
    private boolean readStat;
    private int grant;
    private int receiverID;
    private int wID;
    private int type;
    private String content;
    private LocalDateTime createTime;
    private int inviterID;

}
