package com.eliving.tv.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.LinearLayout
import android.widget.TextView

@SuppressLint("NewApi")
class MarqueeTextView : TextView, Runnable, OnFocusChangeListener {
    /* access modifiers changed from: private */ //    public SWLogger LOG = SWLogger.getLogger(MarqueeTextView.class);
    private var backListener: MarqueeTextCallBackListener? = null
    private var contentWidth = 0
    private var isMeasureContentWidth = false
    private var isPauseScroll = false
    private var isRestartScroll = false
    private var isRun = true
    private val isStop = false
    private var mAllLoopTimes = 30
    private var mCurLoopTimes = 0
    private var mDelayedTime = 120
    private var mMoveSpeed = 40
    private var pointX = -600
    private var scrollToX = 0
    var startScrollRunnable =
        Runnable { //            MarqueeTextView.this.LOG.mo8825d("-----startScrollRunnable----->run()");
            startScrollThread()
        }

    interface MarqueeTextCallBackListener {
        fun onDrawStart()
        fun onDrawStop()
        fun onDrawloop()
    }

    fun getScrollToX(): Int {
        return scrollToX
    }

    fun setScrollToX(scrollToX2: Int) {
        scrollToX = scrollToX2
        pointX = scrollToX2
    }

    fun getmAllLoopTimes(): Int {
        return mAllLoopTimes
    }

    fun setmAllLoopTimes(mAllLoopTimes2: Int) {
        mAllLoopTimes = mAllLoopTimes2
    }

    fun getmMoveSpeed(): Int {
        return mMoveSpeed
    }

    fun setmMoveSpeed(mMoveSpeed2: Int) {
        mMoveSpeed = mMoveSpeed2
    }

    fun getmCurLoopTimes(): Int {
        return mCurLoopTimes
    }

    fun setmCurLoopTimes(mCurLoopTimes2: Int) {
        mCurLoopTimes = mCurLoopTimes2
    }

    fun getContentWidth() {
        contentWidth = paint.measureText(text.toString()).toInt()
    }

    constructor(context: Context?, linearLayout: LinearLayout?) : super(context) {
        isFocusable = false
        isClickable = false
        isDuplicateParentStateEnabled = false
        filterTouchesWhenObscured = false
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        isFocusable = false
        isClickable = false
        isDuplicateParentStateEnabled = false
        filterTouchesWhenObscured = false
        setSingleLine()
    }

    /* access modifiers changed from: protected */
    public override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!isMeasureContentWidth) {
            contentWidth = paint.measureText(text.toString()).toInt()
            isMeasureContentWidth = true
        }
    }

    fun getmDelayedTime(): Int {
        return mDelayedTime
    }

    fun setmDelayedTime(mDelayedTime2: Int) {
        mDelayedTime = mDelayedTime2
    }

    override fun run() {
        if (!isRun) {
            return
        }
        if (scrollToX > contentWidth) {
            scrollToX = pointX
            scrollTo(scrollToX, 0)
            visibility = View.INVISIBLE
            Handler()
                .postDelayed(startScrollRunnable, (mDelayedTime * 1000).toLong())
            if (backListener != null) {
                backListener!!.onDrawloop()
                return
            }
            return
        }
        scrollTo(scrollToX, 0)
        scrollToX += 8
        postDelayed(this, mMoveSpeed.toLong())
        if (visibility != View.VISIBLE) {
            visibility = View.VISIBLE
        }
    }

    fun startScrollThread() {
        if (backListener != null) {
            backListener!!.onDrawStart()
        }
        if (mAllLoopTimes == -1 || mCurLoopTimes < mAllLoopTimes) {
            mCurLoopTimes++
            post(this)
            return
        }
        pauseScroll()
    }

    fun pauseScroll() {
        if (!isPauseScroll) {
            removeCallbacks(this)
            isPauseScroll = true
            isRun = false
            isRestartScroll = false
            mCurLoopTimes = 0
            visibility = View.INVISIBLE
            if (backListener != null) {
                backListener!!.onDrawStop()
            }
        }
    }

    fun startScroll() {
//        Log.("***startScroll*********isRestartScroll==****" + this.isRestartScroll + "*****isRun==****" + this.isRun);
        if (!isRestartScroll || !isRun) {
            isRestartScroll = true
            isRun = true
            isPauseScroll = false
            startScrollThread()
        }
    }

    override fun setText(text: CharSequence, type: BufferType) {
        super.setText(text, type)
    }

    override fun onFocusChange(
        v: View,
        hasFocus: Boolean
    ) {
    }

    fun setMarqueeTextCallBackListener(MarqueeTextCallBackListener2: MarqueeTextCallBackListener?) {
        backListener = MarqueeTextCallBackListener2
    }
}