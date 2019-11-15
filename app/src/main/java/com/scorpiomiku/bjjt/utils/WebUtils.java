package com.scorpiomiku.bjjt.utils;

import java.util.HashMap;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class WebUtils {
    private static WebUtils instance = new WebUtils();
    private static OkHttpClient mClient = new OkHttpClient();

    private WebUtils() {
    }

    public static WebUtils getInstance() {
        return instance;
    }

    private static RequestBody getRequestBody(HashMap<String, String> data) {
        String body = "";
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Set<String> keys = data.keySet();
        for (String key : keys) {
            body += key + "=" + data.get(key) + "&";
        }
        body = body.substring(0, body.length() - 1);
        RequestBody requestBody = RequestBody.create(mediaType, body);
        return requestBody;
    }

    public void registerAndLogin(HashMap<String, String> hashMap, Callback callback) {
        Request request = new Request.Builder().post(getRequestBody(hashMap))
                .url(ConstantUtils.host + "/android/register_login/").build();
        Call call = mClient.newCall(request);
        call.enqueue(callback);
    }

    public void getAudios(Callback callback) {
        Request request = new Request.Builder().get()
                .url(ConstantUtils.host + "/android/get_audios").build();
        Call call = mClient.newCall(request);
        call.enqueue(callback);
    }

    public void getQuestions(Callback callback) {
        Request request = new Request.Builder().get()
                .url(ConstantUtils.host + "/android/get_questions").build();
        Call call = mClient.newCall(request);
        call.enqueue(callback);
    }
}
