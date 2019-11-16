package com.scorpiomiku.bjjt.modules.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.scorpiomiku.bjjt.R;
import com.scorpiomiku.bjjt.base.BaseActivity;
import com.scorpiomiku.bjjt.bean.MyAudio;
import com.scorpiomiku.bjjt.bean.User;
import com.scorpiomiku.bjjt.modules.fragments.SurveyFragment;
import com.scorpiomiku.bjjt.utils.ConstantUtils;
import com.scorpiomiku.bjjt.utils.MessageUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SecondActivity extends BaseActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private ArrayList<String> questions = new ArrayList<>();
    private ArrayList<MyAudio> audios = new ArrayList<>();


    private FragmentPagerAdapter adapter;
    private int fragmentIndex = 0;


    private MediaPlayer player;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        user = (User) getIntent().getSerializableExtra("user");
    }

    @Override
    protected Handler initHandle() {
        @SuppressLint("HandlerLeak") Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        getAudio();
                        break;
                    case 2:
                        adapter.notifyDataSetChanged();
                        viewPager.setCurrentItem(ConstantUtils.lastIndex);
                        break;
                }

            }
        };
        return handler;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void iniview() {
        player = new MediaPlayer();
        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return SurveyFragment.newInstance(questions, audios.get(position), position, player, user);
            }

            @Override
            public int getCount() {
                return audios.size();
            }
        };
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                playMusic(audios.get(i));
                fragmentIndex = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        showDialog();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_second;
    }

    @Override
    public void refreshData() {
        getQuestions();
    }


    /**
     * 两个按钮的 dialog
     */
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("小提示")
                .setMessage("小心滑动列表误触选项，滑动左侧文字不会误触").setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        ConstantUtils.isFirst = false;
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.reset();
        player.release();
        ConstantUtils.isFirst = true;
    }

    private void getQuestions() {
        webUtils.getQuestions(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JsonObject jsonObject = getJsonObj(response);
                int result = jsonObject.get("result").getAsInt();
                if (result == 1) {
                    Gson gson = new Gson();
                    String[] qs = gson.fromJson(jsonObject.get("questions"), String[].class);
                    questions.clear();
                    questions.addAll(Arrays.asList(qs));
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }

    private void getAudio() {
        webUtils.getAudios(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JsonObject jsonObject = getJsonObj(response);
                int result = jsonObject.get("result").getAsInt();
                if (result == 1) {
                    Gson gson = new Gson();
                    MyAudio[] myAudios = gson.fromJson(jsonObject.get("datas"), MyAudio[].class);
                    audios.clear();
                    audios.addAll(Arrays.asList(myAudios));
                    handler.sendEmptyMessage(2);
                }
            }
        });
    }

    private void playMusic(MyAudio myAudio) {
        player.reset();
        AssetManager assetManager = getAssets();
        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = assetManager.openFd(myAudio.getAudio_name());
            player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
                    fileDescriptor.getStartOffset());
            player.setLooping(true);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //跳转到下一页的函数封装
    public void onNextFragment() {
        fragmentIndex = (fragmentIndex + 1);
        if (fragmentIndex >= audios.size()) {
            MessageUtils.makeToast("后面没有啦，退出就行，非常感谢！");
        } else {
            viewPager.setCurrentItem(fragmentIndex);
        }
    }

}
