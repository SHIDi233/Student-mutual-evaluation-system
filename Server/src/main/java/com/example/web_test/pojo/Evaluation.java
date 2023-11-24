package com.example.web_test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evaluation {

    private int eID;
    private int hwID;
    private LocalDateTime ddl;
    private LocalDateTime cTime;

}
