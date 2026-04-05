package com.devsu.hackerearth.backend.account.utils;

public class Utils {
    public static String maskNumber(String number, int visibleDigits) {
        String visible = number.substring(0, visibleDigits);
        String masked = "X".repeat(number.length() - visibleDigits);
        return visible + masked;
    }
}
