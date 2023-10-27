package com.example.web_test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Classes {
    private int classID;
    private String className;
    private int adminID;
    private int numOfMember;
    private String introduction;
    private LocalDateTime createTime;
    private String subject;
    private String code;
}
