package com.scorpiomiku.bjjt.modules.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.scorpiomiku.bjjt.R;
import com.scorpiomiku.bjjt.base.BaseActivity;
import com.scorpiomiku.bjjt.bean.User;
import com.scorpiomiku.bjjt.utils.MessageUtils;
import com.scorpiomiku.bjjt.utils.RandomUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    @BindView(R.id.radio0)
    RadioButton radio0;
    @BindView(R.id.radio1)
    RadioButton radio1;
    @BindView(R.id.rgSex)
    RadioGroup rgSex;
    @BindView(R.id.input_name)
    EditText inputName;
    @BindView(R.id.login)
    Button login;

    private int sex = -1;
    private String name = "...123432";
    private int hasDone;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

    }

    @SuppressLint("HandlerLeak")
    @Override
    protected Handler initHandle() {
        return new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        MessageUtils.logd(hasDone + "");
                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        };
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void iniview() {
        checkPermission();
        inputName.setText(RandomUtils.getRandomName());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void refreshData() {

    }

    @OnClick({R.id.radio0, R.id.radio1, R.id.rgSex, R.id.input_name, R.id.login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.radio0:
                sex = 1;
                break;
            case R.id.radio1:
                sex = 0;
                break;
            case R.id.rgSex:
                break;
            case R.id.input_name:
                break;
            case R.id.login:
                login();
                break;
        }
    }


    private void login() {
        name = inputName.getText().toString().trim();
        if (sex == -1) {
            MessageUtils.makeToast("性别不能不选哟");
        } else if (name.equals("...123432")) {
            MessageUtils.makeToast("姓名不能为空哟，随便起个吧");
        } else {
            HashMap<String, String> data = getData();
            data.put("sex", sex + "");
            data.put("name", name);
            webUtils.loginAndRegister(data, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    JsonObject jsonObject = getJsonObj(response);
                    Gson gson = new Gson();
                    if (jsonObject.get("result").getAsInt() == 1) {
                        user = gson.fromJson(jsonObject.get("user"), User.class);
                        MessageUtils.logd(user.toString());
                        hasDone = jsonObject.get("answer_num").getAsInt();
                        handler.sendEmptyMessage(1);
                    }
                }
            });
        }
    }

    private void checkPermission() {
        //使用兼容库就无需判断系统版本
        int hasWriteStoragePermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED) {
            //拥有权限，执行操作
        } else {
            //没有权限，向用户请求权限
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.SEND_SMS
                    },
                    1);
        }
    }


}
