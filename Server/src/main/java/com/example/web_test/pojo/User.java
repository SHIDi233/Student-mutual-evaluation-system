package com.example.web_test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private int ID;
    private String mail;
    private String Name;
    private String password;
    private int role;
    private String head;
    private String school;
    private String majority;
    private String introduction;
    private String readme;
    private String studentID;

}
