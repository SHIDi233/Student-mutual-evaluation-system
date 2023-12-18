package com.example.web_test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appeal {

    private int apID;
    private int cID;
    private int hwID;
    private int uID;
    private boolean isHandled;
    private String result;
    private String content;

}
