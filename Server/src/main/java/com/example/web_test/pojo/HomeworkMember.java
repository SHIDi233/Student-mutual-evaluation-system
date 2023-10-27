package com.example.web_test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkMember {

    private int hwID;
    private int uID;
    private boolean isSubmit;
    private String content;
    private int cID;

}
