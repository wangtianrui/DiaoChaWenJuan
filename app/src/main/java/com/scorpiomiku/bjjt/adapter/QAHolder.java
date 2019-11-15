package com.scorpiomiku.bjjt.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.scorpiomiku.bjjt.R;
import com.scorpiomiku.bjjt.utils.MessageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ScorpioMiku on 2019/11/13.
 */

public class QAHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.question_title)
    TextView questionTitle;
    @BindView(R.id.ratingbar)
    SimpleRatingBar ratingbar;

    public QAHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindView(String question, SimpleRatingBar.OnRatingBarChangeListener listener) {
        questionTitle.setText(question);
        ratingbar.setOnRatingBarChangeListener(listener);
    }
}
