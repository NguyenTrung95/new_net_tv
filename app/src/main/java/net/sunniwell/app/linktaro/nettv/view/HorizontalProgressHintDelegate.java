package net.sunniwell.app.linktaro.nettv.view;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class HorizontalProgressHintDelegate extends ProgressHintDelegate {
    public HorizontalProgressHintDelegate(SeekBar seekBar, AttributeSet attrs, int defStyleAttr) {
        super(seekBar, attrs, defStyleAttr);
    }

    /* access modifiers changed from: protected */
    public PointF getHintDragCoordinates(MotionEvent event) {
        return new PointF(event.getRawX() - this.mSeekBar.getX(), this.mSeekBar.getY());
    }

    /* access modifiers changed from: protected */
    public Point getFixedHintOffset() {
        return new Point(getHorizontalOffset(this.mSeekBar.getMax() / 2), getVerticalOffset());
    }

    /* access modifiers changed from: protected */
    public Point getFollowHintOffset() {
        return new Point(getHorizontalOffset(this.mSeekBar.getProgress()), getVerticalOffset());
    }

    private int getHorizontalOffset(int progress) {
        return (getFollowPosition(progress) - (this.mPopupView.getMeasuredWidth() / 2)) + (this.mSeekBar.getHeight() / 2);
    }

    private int getVerticalOffset() {
        return -(this.mSeekBar.getHeight() + this.mPopupView.getMeasuredHeight() + this.mPopupOffset);
    }
}
