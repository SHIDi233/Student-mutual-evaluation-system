package com.example.web_test.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncodeUtils {

    public static String bCryptEncode(String str){
        if(str == null){
            return null;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String result = encoder.encode(str);
        return result;
    }


    public static boolean bCryptMatch(String str, String bCryptStr){
        if(str == null){
            return false;
        }
        if(bCryptStr == null){
            return false;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        encoder.upgradeEncoding(bCryptStr);
        return encoder.matches(str, bCryptStr);
    }


}
