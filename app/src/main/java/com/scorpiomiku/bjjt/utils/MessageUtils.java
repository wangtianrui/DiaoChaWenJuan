package com.scorpiomiku.bjjt.utils;

import android.util.Log;
import android.widget.Toast;

import com.scorpiomiku.bjjt.BJJT;

/**
 * Created by ScorpioMiku on 2019/6/22.
 */

public class MessageUtils {
    private static final String TAG = "MessageUtils";

    public static void makeToast(String message) {
        Toast.makeText(BJJT.instance, message, Toast.LENGTH_SHORT).show();
    }

    public static void logd(String message) {
        Log.d(TAG, message);
    }

    public static void loge(Exception e) {
        Log.e(TAG, "loge: " + e.getMessage());
    }
}
