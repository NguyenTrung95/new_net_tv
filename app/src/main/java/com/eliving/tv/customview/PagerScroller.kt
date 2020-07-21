package com.eliving.tv.customview

import android.content.Context
import android.view.animation.Interpolator
import android.widget.Scroller

class PagerScroller : Scroller {
    private var mDuration = 1500

    constructor(context: Context?) : super(context) {}
    constructor(
        context: Context?,
        interpolator: Interpolator?
    ) : super(context, interpolator) {
    }

    override fun startScroll(
        startX: Int,
        startY: Int,
        dx: Int,
        dy: Int,
        duration: Int
    ) {
        super.startScroll(startX, startY, dx, dy, mDuration)
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        super.startScroll(startX, startY, dx, dy, mDuration)
    }

    fun setmDuration(time: Int) {
        mDuration = time
    }

    fun getmDuration(): Int {
        return mDuration
    }
}