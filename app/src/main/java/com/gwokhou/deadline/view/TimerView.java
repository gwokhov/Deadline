package com.gwokhou.deadline.view;

import android.content.Context;
import android.util.AttributeSet;

import com.gwokhou.deadline.TimerUpdateListener;

import cn.iwgang.countdownview.CountdownView;

public class TimerView extends CountdownView {
    private TimerUpdateListener mTimerUpdateListener;

    public TimerView(Context context) {
        super(context);
    }

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void updateShow(long ms) {
        super.updateShow(ms);

        if (mTimerUpdateListener != null) {
            mTimerUpdateListener.onUpdateView(ms);
        }
    }

    public void setTimerUpdateListener(TimerUpdateListener timerUpdateListener) {
        mTimerUpdateListener = timerUpdateListener;
    }
}
