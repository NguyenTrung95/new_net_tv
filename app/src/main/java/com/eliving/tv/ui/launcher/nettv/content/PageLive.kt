package com.eliving.tv.ui.launcher.nettv.content

import android.content.Context
import android.os.Message
import android.view.LayoutInflater
import com.eliving.tv.R
import com.eliving.tv.ui.launcher.nettv.activity.NettvActivity

class PageLive(
    context: Context?,
    nettvActivity: NettvActivity
) : PageBase(context) {
       var mContentView:android.view.View? = null
          get() = field
          set(value) {
              field = value
          }

    override fun doByMessage(message: Message?, i: Int) {
        TODO("Not yet implemented")
    }

    override fun onCreate(context: Context?) {
        mContentView = (context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.page_live, this)
        
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        TODO("Not yet implemented")
    }

    override fun onStop() {
        TODO("Not yet implemented")
    }
    interface PageLiveCallBackListener {
        fun livePageCallBack(i: Int, message: Message?)
    }
}