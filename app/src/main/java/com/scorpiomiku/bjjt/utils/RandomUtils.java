package com.scorpiomiku.bjjt.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RandomUtils {
    public static String getRandomName() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:HH:mm:ss");
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        int randomInt = (new Random()).nextInt();
        return simpleDateFormat.format(date) + String.valueOf(randomInt).substring(1);
    }
}
