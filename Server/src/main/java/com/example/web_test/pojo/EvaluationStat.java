package com.example.web_test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationStat {

    private int uID;
    private int evaluateNum;
    private int score;
    private int hwID;
    private int evaluatedNum;

}
