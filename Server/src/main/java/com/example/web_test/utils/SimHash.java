package com.example.web_test.utils;

import java.math.BigInteger;

public class SimHash {
    private String text;
    private BigInteger hash;
    private int hashbits;

    public SimHash(String text, int hashbits) {
        this.text = text;
        this.hashbits = hashbits;
        this.hash = this.simHash();
    }

    public BigInteger simHash() {
        int[] hashbits = new int[this.hashbits];
        String[] words = this.text.split("\\s+");
        for (String word : words) {
            BigInteger wordHash = this.hash(word);
            for (int i = 0; i < this.hashbits; i++) {
                BigInteger bitmask = BigInteger.valueOf(1).shiftLeft(i);
                if (wordHash.and(bitmask).signum() != 0) {
                    hashbits[i] += 1;
                } else {
                    hashbits[i] -= 1;
                }
            }
        }
        BigInteger fingerprint = BigInteger.ZERO;
        for (int i = 0; i < this.hashbits; i++) {
            if (hashbits[i] >= 0) {
                fingerprint = fingerprint.add(BigInteger.valueOf(1).shiftLeft(i));
            }
        }
        return fingerprint;
    }

    private BigInteger hash(String word) {
        if (word == null || word.length() == 0) {
            return BigInteger.ZERO;
        }
        char[] wordArray = word.toCharArray();
        BigInteger x = BigInteger.valueOf(((long) wordArray[0]) << 7);
        BigInteger m = BigInteger.valueOf(1000003);
        BigInteger mask = BigInteger.valueOf(2).pow(this.hashbits).subtract(BigInteger.ONE);
        for (char c : wordArray) {
            BigInteger temp = BigInteger.valueOf((long) c);
            x = x.multiply(m).xor(temp).and(mask);
        }
        x = x.xor(BigInteger.valueOf(word.length()));
        if (x.equals(BigInteger.valueOf(-1))) {
            x = BigInteger.valueOf(-2);
        }
        return x;
    }

    public int hammingDistance(SimHash other) {
        BigInteger x = this.hash.xor(other.hash);
        int distance = 0;
        while (x.signum() != 0) {
            distance += 1;
            x = x.and(x.subtract(BigInteger.ONE));
        }
        return distance;
    }

    public static void main(String[] args) {
        String text1 = "中标公司";
        String text2 = "招标公司";
        SimHash simHash1 = new SimHash(text1, 64);
        SimHash simHash2 = new SimHash(text2, 64);
        int distance = simHash1.hammingDistance(simHash2);
        System.out.println("Hamming distance: " + distance);
    }
}
