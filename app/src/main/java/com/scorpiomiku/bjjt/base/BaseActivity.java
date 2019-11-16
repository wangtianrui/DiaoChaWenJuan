package com.scorpiomiku.bjjt.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scorpiomiku.bjjt.utils.MessageUtils;
import com.scorpiomiku.bjjt.utils.WebUtils;

import java.io.IOException;
import java.util.HashMap;

import butterknife.ButterKnife;
import okhttp3.Response;

/**
 * Created by ScorpioMiku on 2019/6/22.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Handler handler;
    protected WebUtils webUtils;
    private HashMap<String, String> data = new HashMap<>();

    public HashMap<String, String> getData() {
        data.clear();
        return data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        webUtils = WebUtils.getInstance();
        ButterKnife.bind(this);
        iniview();
        handler = initHandle();
        refreshData();
    }

    protected abstract Handler initHandle();

    abstract public void iniview();

    abstract public int getLayoutId();

    abstract public void refreshData();

    protected JsonObject getJsonObj(Response response) throws IOException {
        String result = response.body().string();
        MessageUtils.logd(result);
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(result);
        return jsonObject;

    }

}
