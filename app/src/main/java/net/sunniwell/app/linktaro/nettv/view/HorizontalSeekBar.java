package net.sunniwell.app.linktaro.nettv.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import net.sunniwell.app.linktaro.nettv.view.ProgressHintDelegate.SeekBarHintDelegateHolder;

public class HorizontalSeekBar extends SeekBar implements SeekBarHintDelegateHolder {
    private ProgressHintDelegate hintDelegate;

    public HorizontalSeekBar(Context context) {
        super(context);
        init(null, 0);
    }

    public HorizontalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public HorizontalSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        if (!isInEditMode()) {
            this.hintDelegate = new HorizontalProgressHintDelegate(this, attrs, defStyle);
        }
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        super.setOnSeekBarChangeListener(this.hintDelegate.setOnSeekBarChangeListener(l));
    }

    public ProgressHintDelegate getHintDelegate() {
        return this.hintDelegate;
    }
}
