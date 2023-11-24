package com.example.web_test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationMember {

    private int eID;
    private int uID;
    private Integer score;
    private String comment;
    private int desID;
    private int hwID;
    private String image;

}
