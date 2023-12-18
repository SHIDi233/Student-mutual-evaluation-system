package com.example.web_test.utils.DuplicateCheck;

public class Hamming {

        /**
         * 输入两个simHash值（simHash1、simHash2），计算它们的海明距离
         * 返回海明距离
         */
        public static int getHammingDistance(String simHash1, String simHash2) {
            int distance = 0;
            if (simHash1.length() != simHash2.length()) {
                // 出错，返回-1
                distance = -1;
            } else {
                for (int i = 0; i < simHash1.length(); i++) {
                    // 每一位进行比较
                    if (simHash1.charAt(i) != simHash2.charAt(i)) {
                        distance++;
                    }
                }
            }
            return distance;
        }

        /**
         * 输入两个simHash值，输出相似度
         * @param simHash1
         * @param simHash2
         * @return 相似度
         */
        public static double getSimilarity(String simHash1, String simHash2) {
            // 通过 simHash1 和 simHash2 获得它们的海明距离
            int distance = getHammingDistance(simHash1, simHash2);
            // 通过海明距离计算出相似度，并返回
            double tmp = 100 - (double) (distance * 100) / 128;
            tmp = Math.pow(tmp / 10, 2);
            tmp = Math.pow(tmp / 10, 2);
            return Math.floor(tmp) * 0.01;
        }

    }


