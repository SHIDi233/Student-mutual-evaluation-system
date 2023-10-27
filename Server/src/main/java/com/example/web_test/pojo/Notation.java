package com.example.web_test.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notation {

    private int ID;
    private String type;//公告类型
    private boolean isRead;//是否已读
    private String senderName;//发送者名称
    private String scope;//公告范围
    private String content;//公告内容
    private LocalDateTime sendTime;//发送时间
    private int receiverID;
    private int approveID;
}
