package com.scorpiomiku.bjjt.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.scorpiomiku.bjjt.R;
import com.scorpiomiku.bjjt.bean.MyAudio;
import com.scorpiomiku.bjjt.modules.fragments.SurveyFragment;
import com.scorpiomiku.bjjt.utils.MessageUtils;

import java.util.List;

public class QAAdapter extends RecyclerView.Adapter<QAHolder> {
    private Context context;
    private List<String> list;
    private MyAudio audio;
    private SurveyFragment fragment;

    public QAAdapter(Context context, List<String> list, MyAudio audio, SurveyFragment fragment) {
        this.context = context;
        this.list = list;
        this.audio = audio;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public QAHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, viewGroup, false);
        return new QAHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QAHolder qaHolder, int i) {
        qaHolder.bindView(list.get(i), new SimpleRatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(SimpleRatingBar simpleRatingBar, float rating, boolean fromUser) {
                MessageUtils.logd(rating + ":" + i + ":" + audio.getAudio_id());
                fragment.changeScore(rating, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
