package net.sunniwell.app.linktaro.tools;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class PagerScroller extends Scroller {
    private int mDuration = 1500;

    public PagerScroller(Context context) {
        super(context);
    }

    public PagerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, this.mDuration);
    }

    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, this.mDuration);
    }

    public void setmDuration(int time) {
        this.mDuration = time;
    }

    public int getmDuration() {
        return this.mDuration;
    }
}
