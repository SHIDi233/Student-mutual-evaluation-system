package com.example.web_test.utils.DuplicateCheck;

public class DuplicateCheckUtils {
    public static double getRate(String str0, String str1) {

        // 由字符串得出对应的 simHash值
        String simHash0 = SimHash.getSimHash(str0);
        String simHash1 = SimHash.getSimHash(str1);

        // 由 simHash值求出相似度
        return Hamming.getSimilarity(simHash0, simHash1);
    }
}
