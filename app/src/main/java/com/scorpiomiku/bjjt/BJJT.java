package com.scorpiomiku.bjjt;

import android.annotation.SuppressLint;
import android.app.Application;

/**
 * Created by ScorpioMiku on 2019/11/10.
 */

public class BJJT extends Application {
    public static BJJT instance;

    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
