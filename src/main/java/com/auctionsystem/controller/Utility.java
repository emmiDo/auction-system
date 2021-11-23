package com.auctionsystem.controller;

public class Utility {

    static final double THRESHOLD = .0001;

    public static boolean doubleEqualCheck(double d1, double d2) {
        if (Math.abs(d1 - d2) < THRESHOLD)
            return true;

        return false;
    }

}
