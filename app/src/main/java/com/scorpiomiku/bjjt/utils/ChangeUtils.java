package com.scorpiomiku.bjjt.utils;

public class ChangeUtils {
    public static String array2String(float[] array) {
        StringBuilder temp = new StringBuilder();
        for (float i : array) {
            if (i == -1) {
                return "x";
            }
            temp.append(i);
            temp.append(";");
        }
        return temp.toString();
    }
}
