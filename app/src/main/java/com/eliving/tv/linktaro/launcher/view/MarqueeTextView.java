package com.eliving.tv.linktaro.launcher.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;



@SuppressLint({"NewApi"})
public class MarqueeTextView extends TextView implements Runnable, OnFocusChangeListener {
    /* access modifiers changed from: private */
    private MarqueeTextCallBackListener backListener;
    private int contentWidth = 0;
    private boolean isMeasureContentWidth = false;
    private boolean isPauseScroll;
    private boolean isRestartScroll;
    private boolean isRun = true;
    private boolean isStop = false;
    private int mAllLoopTimes = 30;
    private int mCurLoopTimes = 0;
    private int mDelayedTime = 120;
    private int mMoveSpeed = 40;
    private int pointX = -600;
    private int scrollToX;
    Runnable startScrollRunnable = new Runnable() {
        public void run() {
            MarqueeTextView.this.startScrollThread();
        }
    };

    public interface MarqueeTextCallBackListener {
        void onDrawStart();

        void onDrawStop();

        void onDrawloop();
    }

    public int getScrollToX() {
        return this.scrollToX;
    }

    public void setScrollToX(int scrollToX2) {
        this.scrollToX = scrollToX2;
        this.pointX = scrollToX2;
    }

    public int getmAllLoopTimes() {
        return this.mAllLoopTimes;
    }

    public void setmAllLoopTimes(int mAllLoopTimes2) {
        this.mAllLoopTimes = mAllLoopTimes2;
    }

    public int getmMoveSpeed() {
        return this.mMoveSpeed;
    }

    public void setmMoveSpeed(int mMoveSpeed2) {
        this.mMoveSpeed = mMoveSpeed2;
    }

    public int getmCurLoopTimes() {
        return this.mCurLoopTimes;
    }

    public void setmCurLoopTimes(int mCurLoopTimes2) {
        this.mCurLoopTimes = mCurLoopTimes2;
    }

    public void getContentWidth() {
        this.contentWidth = (int) getPaint().measureText(getText().toString());
    }

    public MarqueeTextView(Context context, LinearLayout linearLayout) {
        super(context);
        setFocusable(false);
        setClickable(false);
        setDuplicateParentStateEnabled(false);
        setFilterTouchesWhenObscured(false);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(false);
        setClickable(false);
        setDuplicateParentStateEnabled(false);
        setFilterTouchesWhenObscured(false);
        setSingleLine();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!this.isMeasureContentWidth) {
            this.contentWidth = (int) getPaint().measureText(getText().toString());
            this.isMeasureContentWidth = true;
        }
    }

    public int getmDelayedTime() {
        return this.mDelayedTime;
    }

    public void setmDelayedTime(int mDelayedTime2) {
        this.mDelayedTime = mDelayedTime2;
    }

    public void run() {
        if (!this.isRun) {
            return;
        }
        if (this.scrollToX > this.contentWidth) {
            this.scrollToX = this.pointX;
            scrollTo(this.scrollToX, 0);
            setVisibility(View.INVISIBLE);
            new Handler().postDelayed(this.startScrollRunnable, (long) (this.mDelayedTime * 1000));
            if (this.backListener != null) {
                this.backListener.onDrawloop();
                return;
            }
            return;
        }
        scrollTo(this.scrollToX, 0);
        this.scrollToX += 8;
        postDelayed(this, (long) this.mMoveSpeed);
        if (getVisibility() != View.VISIBLE) {
            setVisibility(View.VISIBLE);
        }
    }

    public void startScrollThread() {
        if (this.backListener != null) {
            this.backListener.onDrawStart();
        }
        if (this.mAllLoopTimes == -1 || this.mCurLoopTimes < this.mAllLoopTimes) {
            this.mCurLoopTimes++;
            post(this);
            return;
        }
        pauseScroll();
    }

    public void pauseScroll() {
        if (!this.isPauseScroll) {
            removeCallbacks(this);
            this.isPauseScroll = true;
            this.isRun = false;
            this.isRestartScroll = false;
            this.mCurLoopTimes = 0;
            setVisibility(View.INVISIBLE);
            if (this.backListener != null) {
                this.backListener.onDrawStop();
            }
        }
    }

    public void startScroll() {
        if (!this.isRestartScroll || !this.isRun) {
            this.isRestartScroll = true;
            this.isRun = true;
            this.isPauseScroll = false;
            startScrollThread();
        }
    }

    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

    public void onFocusChange(View v, boolean hasFocus) {
    }

    public void setMarqueeTextCallBackListener(MarqueeTextCallBackListener MarqueeTextCallBackListener2) {
        this.backListener = MarqueeTextCallBackListener2;
    }
}
