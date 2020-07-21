package com.eliving.tv.ui.launcher.nettv.content

import android.content.Context
import android.os.Message
import android.widget.LinearLayout

abstract class PageBase(context: Context?) :
    LinearLayout(context) {
    abstract fun doByMessage(message: Message?, i: Int)
    abstract fun onCreate(context: Context?)
    abstract fun onDestroy()
    abstract fun onResume()
    abstract fun onStop()

    init {
        onCreate(context)
    }
}