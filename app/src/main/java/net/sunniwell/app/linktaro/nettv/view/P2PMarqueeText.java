package net.sunniwell.app.linktaro.nettv.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class P2PMarqueeText extends TextView implements Runnable, OnFocusChangeListener {
    private int contentWidth = 0;
    private int delayTime = 120000;
    private boolean isMeasureContentWidth = false;
    private boolean isPauseScroll;
    private boolean isRestartScroll;
    private boolean isRun = true;
    private boolean isStop = false;
    private int mDelayedTime = 120;
    private int mMoveSpeed = 3;
    private int scrollToX = -1200;

    public P2PMarqueeText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(false);
        setClickable(false);
        setDuplicateParentStateEnabled(false);
        setFilterTouchesWhenObscured(false);
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
        if (this.isRun) {
            if (getVisibility() != 0) {
                setVisibility(0);
            }
            if (this.scrollToX > this.contentWidth) {
                this.scrollToX = -1200;
                scrollTo(this.scrollToX, 0);
                setVisibility(4);
                postDelayed(this, (long) (this.mDelayedTime * 1000));
                return;
            }
            scrollTo(this.scrollToX, 0);
            this.scrollToX += this.mMoveSpeed;
            postDelayed(this, 5);
        }
    }

    public void startScroll() {
        post(this);
    }

    public void pauseScroll() {
        if (!this.isPauseScroll) {
            removeCallbacks(this);
            this.isPauseScroll = true;
            this.isRun = false;
            this.isRestartScroll = false;
            setVisibility(4);
        }
    }

    public void restartScroll() {
        if (!this.isRestartScroll || !this.isRun) {
            this.isRestartScroll = true;
            this.isRun = true;
            this.scrollToX = -1200;
            this.isPauseScroll = false;
            startScroll();
        }
    }

    public boolean onTouchEvent(MotionEvent arg0) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean arg0, int arg1, Rect arg2) {
    }

    public boolean onFilterTouchEventForSecurity(MotionEvent event) {
        return false;
    }

    /* access modifiers changed from: protected */
    public void onScrollChanged(int horiz, int vert, int oldHoriz, int oldVert) {
    }

    /* access modifiers changed from: protected */
    public void onSelectionChanged(int selStart, int selEnd) {
    }

    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
    }

    public void onHoverChanged(boolean hovered) {
    }

    /* access modifiers changed from: protected */
    public void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
    }

    public void onFocusChange(View arg0, boolean arg1) {
    }
}
