package com.example.web_test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse {

    private int wID;
    private String wName;
    private int adminID;
    private String wPath;
    private LocalDateTime cTime;
    private LocalDateTime uTime;
    private String describe;

}
