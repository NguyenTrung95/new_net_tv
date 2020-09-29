package net.sunniwell.app.linktaro.nettv.view;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build.VERSION;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import net.sunniwell.app.linktaro.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class ProgressHintDelegate implements OnSeekBarChangeListener {
    public static final SeekBarHintAdapter DEFAULT_HINT_ADAPTER = new SeekBarHintAdapter() {
        public String getHint(SeekBar seekBar, int progress) {
            return String.valueOf(progress);
        }
    };
    public static final int POPUP_FIXED = 1;
    public static final int POPUP_FOLLOW = 0;
    private Handler handler = new Handler();
    private boolean isTracking;
    /* access modifiers changed from: private */
    public ProxyChangeListener listener = new ProxyChangeListener(null);
    private SeekBarHintAdapter mHintAdapter;
    /* access modifiers changed from: private */
    public SeekBarHintAttacher mHintAttacher;
    protected PopupWindow mPopup;
    private boolean mPopupAlwaysShown;
    private int mPopupAnimStyle;
    private boolean mPopupDraggable;
    private int mPopupLayout;
    protected int mPopupOffset;
    protected int mPopupStyle;
    protected TextView mPopupTextView;
    protected View mPopupView;
    protected SeekBar mSeekBar;
    private OnTouchListener popupTouchProxy = new OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            PointF coordinates = ProgressHintDelegate.this.getHintDragCoordinates(event);
            return ProgressHintDelegate.this.mSeekBar.dispatchTouchEvent(MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), coordinates.x, coordinates.y, event.getMetaState()));
        }
    };

    @Retention(RetentionPolicy.SOURCE)
    public @interface PopupStyle {
    }

    private static class ProxyChangeListener implements OnSeekBarChangeListener {
        private OnSeekBarChangeListener mExternalListener;
        private OnSeekBarChangeListener mInternalListener;

        private ProxyChangeListener() {
        }

        /* synthetic */ ProxyChangeListener(ProxyChangeListener proxyChangeListener) {
            this();
        }

        public void setInternalListener(OnSeekBarChangeListener l) {
            this.mInternalListener = l;
        }

        public void setExternalListener(OnSeekBarChangeListener l) {
            this.mExternalListener = l;
        }

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (this.mInternalListener != null) {
                this.mInternalListener.onProgressChanged(seekBar, progress, fromUser);
            }
            if (this.mExternalListener != null) {
                this.mExternalListener.onProgressChanged(seekBar, progress, fromUser);
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            if (this.mInternalListener != null) {
                this.mInternalListener.onStartTrackingTouch(seekBar);
            }
            if (this.mExternalListener != null) {
                this.mExternalListener.onStartTrackingTouch(seekBar);
            }
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (this.mInternalListener != null) {
                this.mInternalListener.onStopTrackingTouch(seekBar);
            }
            if (this.mExternalListener != null) {
                this.mExternalListener.onStopTrackingTouch(seekBar);
            }
        }
    }

    public interface SeekBarHintAdapter {
        String getHint(SeekBar seekBar, int i);
    }

    public interface SeekBarHintAttacher {
        void onAttached();
    }

    public interface SeekBarHintDelegateHolder {
        ProgressHintDelegate getHintDelegate();
    }

    /* access modifiers changed from: protected */
    public abstract Point getFixedHintOffset();

    /* access modifiers changed from: protected */
    public abstract Point getFollowHintOffset();

    /* access modifiers changed from: protected */
    public abstract PointF getHintDragCoordinates(MotionEvent motionEvent);

    public ProgressHintDelegate(SeekBar seekBar, int mPopupLayout2, int mPopupOffset2, boolean mPopupAlwaysShown2, boolean mPopupDraggable2, int mPopupStyle2, int mPopupAnimStyle2) {
        initDelegate(seekBar, mPopupLayout2, mPopupOffset2, mPopupAlwaysShown2, mPopupDraggable2, mPopupStyle2, mPopupAnimStyle2, DEFAULT_HINT_ADAPTER);
    }

    public ProgressHintDelegate(SeekBar seekBar, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = seekBar.getContext().obtainStyledAttributes(attrs, R.styleable.ProgressHint, defStyleAttr, R.style.ProgressHint);
        int mPopupLayout2 = a.getResourceId(0, R.layout.progress_hint_popup);
        int mPopupOffset2 = (int) a.getDimension(1, 0.0f);
        int mPopupStyle2 = a.getInt(2, 0);
        int mPopupAnimStyle2 = a.getResourceId(3, R.style.ProgressHintPopupAnimation);
        boolean mPopupAlwaysShown2 = a.getBoolean(4, false);
        boolean mPopupDraggable2 = a.getBoolean(5, false);
        a.recycle();
        initDelegate(seekBar, mPopupLayout2, mPopupOffset2, mPopupAlwaysShown2, mPopupDraggable2, mPopupStyle2, mPopupAnimStyle2, DEFAULT_HINT_ADAPTER);
    }

    private void initDelegate(SeekBar seekBar, int mPopupLayout2, int mPopupOffset2, boolean mPopupAlwaysShown2, boolean mPopupDraggable2, int mPopupStyle2, int mPopupAnimStyle2, SeekBarHintAdapter mHintAdapter2) {
        this.mSeekBar = seekBar;
        this.mPopupLayout = mPopupLayout2;
        this.mPopupOffset = mPopupOffset2;
        this.mPopupAlwaysShown = mPopupAlwaysShown2;
        this.mPopupDraggable = mPopupDraggable2;
        this.mPopupStyle = mPopupStyle2;
        this.mPopupAnimStyle = mPopupAnimStyle2;
        this.mHintAdapter = mHintAdapter2;
        initHintPopup();
        attachSeekBar();
    }

    private void initHintPopup() {
        String popupText = null;
        if (this.mHintAdapter != null) {
            popupText = this.mHintAdapter.getHint(this.mSeekBar, this.mSeekBar.getProgress());
        }
        this.mPopupView = ((LayoutInflater) this.mSeekBar.getContext().getSystemService("layout_inflater")).inflate(this.mPopupLayout, null);
        this.mPopupView.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
        this.mPopupTextView = (TextView) this.mPopupView.findViewById(16908308);
        TextView textView = this.mPopupTextView;
        if (popupText == null) {
            popupText = String.valueOf(this.mSeekBar.getProgress());
        }
        textView.setText(popupText);
        this.mPopup = new PopupWindow(this.mPopupView, -2, -2, false);
        this.mPopup.setAnimationStyle(this.mPopupAnimStyle);
    }

    private void attachSeekBar() {
        final OnGlobalLayoutListener layoutListener = new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (ProgressHintDelegate.this.mSeekBar.getVisibility() != 0) {
                    ProgressHintDelegate.this.hidePopup();
                } else {
                    ProgressHintDelegate.this.checkInitialState();
                }
            }
        };
        this.mSeekBar.addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            public void onViewAttachedToWindow(View v) {
                ProgressHintDelegate.this.mSeekBar.setOnSeekBarChangeListener(ProgressHintDelegate.this.listener);
                ProgressHintDelegate.this.mSeekBar.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
                if (ProgressHintDelegate.this.mHintAttacher != null) {
                    ProgressHintDelegate.this.mHintAttacher.onAttached();
                }
            }

            @SuppressLint({"NewApi"})
            public void onViewDetachedFromWindow(View v) {
                if (VERSION.SDK_INT < 16) {
                    ProgressHintDelegate.this.mSeekBar.getViewTreeObserver().removeGlobalOnLayoutListener(layoutListener);
                } else {
                    ProgressHintDelegate.this.mSeekBar.getViewTreeObserver().removeOnGlobalLayoutListener(layoutListener);
                }
                ProgressHintDelegate.this.hidePopup();
            }
        });
        this.listener.setInternalListener(this);
    }

    /* access modifiers changed from: private */
    public void checkInitialState() {
        setPopupAlwaysShown(this.mPopupAlwaysShown);
        setPopupDraggable(this.mPopupDraggable);
    }

    public void showPopup() {
        this.handler.post(new Runnable() {
            public void run() {
                ProgressHintDelegate.this.showPopupInternally();
            }
        });
    }

    /* access modifiers changed from: private */
    public void showPopupInternally() {
        Point offsetPoint = null;
        switch (this.mPopupStyle) {
            case 0:
                offsetPoint = getFollowHintOffset();
                break;
            case 1:
                offsetPoint = getFixedHintOffset();
                break;
        }
        this.mPopup.showAtLocation(this.mSeekBar, 0, 0, 0);
        this.mPopup.update(this.mSeekBar, offsetPoint.x, offsetPoint.y, -1, -1);
    }

    public void hidePopup() {
        this.handler.removeCallbacksAndMessages(null);
        if (this.mPopup.isShowing()) {
            this.mPopup.dismiss();
        }
    }

    @LayoutRes
    public int getPopupLayout() {
        return this.mPopupLayout;
    }

    @LayoutRes
    public void setPopupLayout(int layout) {
        this.mPopupLayout = layout;
        if (this.mPopup != null) {
            this.mPopup.dismiss();
        }
        initHintPopup();
        checkInitialState();
    }

    public int getPopupStyle() {
        return this.mPopupStyle;
    }

    public void setPopupStyle(int style) {
        this.mPopupStyle = style;
        if (this.mPopupAlwaysShown) {
            showPopup();
        }
    }

    public boolean isPopupAlwaysShown() {
        return this.mPopupAlwaysShown;
    }

    public void setPopupAlwaysShown(boolean alwaysShown) {
        this.mPopupAlwaysShown = alwaysShown;
        if (alwaysShown) {
            showPopup();
        } else if (!this.isTracking) {
            hidePopup();
        }
    }

    public boolean isPopupDraggable() {
        return this.mPopupDraggable;
    }

    public void setPopupDraggable(boolean draggable) {
        this.mPopupDraggable = draggable;
        if (this.mPopupView != null) {
            this.mPopupView.setOnTouchListener(draggable ? this.popupTouchProxy : null);
        }
    }

    public void setHintAdapter(SeekBarHintAdapter adapter) {
        this.mHintAdapter = adapter;
        if (this.mPopupTextView != null) {
            this.mPopupTextView.setText(this.mHintAdapter.getHint(this.mSeekBar, this.mSeekBar.getProgress()));
        }
    }

    public void setHintAttacher(SeekBarHintAttacher listener2) {
        this.mHintAttacher = listener2;
    }

    public void setProgress(int pos) {
        this.mSeekBar.setProgress(pos);
    }

    public OnSeekBarChangeListener setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        if (l instanceof ProxyChangeListener) {
            this.listener = (ProxyChangeListener) l;
        } else {
            this.listener.setExternalListener(l);
        }
        return this.listener;
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        this.isTracking = true;
        showPopupInternally();
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        this.isTracking = false;
        if (!this.mPopupAlwaysShown) {
            hidePopup();
        }
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        String popupText = null;
        if (this.mHintAdapter != null) {
            popupText = this.mHintAdapter.getHint(this.mSeekBar, progress);
        }
        TextView textView = this.mPopupTextView;
        if (popupText == null) {
            popupText = String.valueOf(progress);
        }
        textView.setText(popupText);
        if (this.mPopupStyle == 0) {
            Point offsetPoint = getFollowHintOffset();
            this.mPopup.update(this.mSeekBar, offsetPoint.x, offsetPoint.y, -1, -1);
        }
    }

    /* access modifiers changed from: protected */
    public int getFollowPosition() {
        return getFollowPosition(this.mSeekBar.getProgress());
    }

    /* access modifiers changed from: protected */
    public int getFollowPosition(int progress) {
        return (int) (((float) (((this.mSeekBar.getWidth() - this.mSeekBar.getPaddingLeft()) - this.mSeekBar.getPaddingRight()) * progress)) / ((float) this.mSeekBar.getMax()));
    }
}
