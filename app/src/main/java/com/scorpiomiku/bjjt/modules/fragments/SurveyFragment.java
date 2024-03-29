package com.scorpiomiku.bjjt.modules.fragments;

import android.annotation.SuppressLint;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.scorpiomiku.bjjt.R;
import com.scorpiomiku.bjjt.adapter.QAAdapter;
import com.scorpiomiku.bjjt.base.BaseFragment;
import com.scorpiomiku.bjjt.bean.MyAudio;
import com.scorpiomiku.bjjt.bean.User;
import com.scorpiomiku.bjjt.modules.activities.SecondActivity;
import com.scorpiomiku.bjjt.utils.ChangeUtils;
import com.scorpiomiku.bjjt.utils.ConstantUtils;
import com.scorpiomiku.bjjt.utils.MessageUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SurveyFragment extends BaseFragment {
    @BindView(R.id.pause_music)
    ImageView pauseMusic;
    @BindView(R.id.play_music)
    ImageView playMusic;
    @BindView(R.id.percent_num)
    TextView percentNum;
    @BindView(R.id.audio_name)
    TextView audioName;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.total_num)
    TextView totalNum;
    @BindView(R.id.next)
    Button next;

    private User user;
    private List<String> questions = new ArrayList<>();
    private QAAdapter adapter;
    private MyAudio myAudio;
    private int index;
    private MediaPlayer player;
    private float[] answers;
    private String answer;

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    public void setMyAudio(MyAudio myAudio) {
        this.myAudio = myAudio;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setPlayer(MediaPlayer player) {
        this.player = player;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static SurveyFragment newInstance(List<String> questions, MyAudio myAudio, int index, MediaPlayer player, User user) {
        SurveyFragment surveyFragment = new SurveyFragment();
        surveyFragment.setQuestions(questions);
        surveyFragment.setMyAudio(myAudio);
        surveyFragment.setIndex(index);
        surveyFragment.setPlayer(player);
        surveyFragment.setUser(user);
        return surveyFragment;
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
                        ((SecondActivity) getActivity()).onNextFragment();
                        break;
                }
            }
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.survey_fragment;
    }

    @Override
    protected void refreshData() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        adapter = new QAAdapter(getContext(), questions, myAudio, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (index == ConstantUtils.lastIndex) {
            starMusic();
        }
        percentNum.setText(myAudio.getAudio_id() + "");
        audioName.setText(myAudio.getAudio_name());
        totalNum.setText("/" + ConstantUtils.totalAudioNumber);
        answers = new float[questions.size()];
        Arrays.fill(answers, -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.pause_music, R.id.play_music, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pause_music:
                pauseMusic();
                break;
            case R.id.play_music:
                starMusic();
                break;
            case R.id.next:
                if (answer.equals("x")) {
                    MessageUtils.makeToast("问题还没有回答完哟~不能有空哟~");
                } else {
                    upAnswer();
                    handler.sendEmptyMessage(1);
                }
        }
    }

    private void playMusic() {
        player.reset();
        AssetManager assetManager = getActivity().getAssets();
        AssetFileDescriptor fileDescriptor = null;
        try {
            MessageUtils.logd(myAudio.getAudio_name());
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

    private void starMusic() {
        playMusic.setVisibility(View.INVISIBLE);
        pauseMusic.setVisibility(View.VISIBLE);
        playMusic();
    }

    private void pauseMusic() {
        playMusic.setVisibility(View.VISIBLE);
        pauseMusic.setVisibility(View.INVISIBLE);
        player.pause();
    }

    public void changeScore(float score, int index) {
        answers[index] = score;
        answer = ChangeUtils.array2String(answers);
        MessageUtils.logd(answer + ":" + myAudio.getAudio_name());
    }

    private void upAnswer() {
        HashMap<String, String> data = getData();
        data.put("audioName", myAudio.getAudio_name());
        data.put("answer", answer);
        data.put("userId", user.getId() + "");
        webUtils.upAnswer(data, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }


}
