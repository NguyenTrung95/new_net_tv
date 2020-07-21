package com.eliving.tv.ui.dlna

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.eliving.tv.R
import com.eliving.tv.base.constant.Constant
import com.eliving.tv.ui.channels.ChanelType

class DLNAActivity : Activity() {

    /* access modifiers changed from: protected */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad)

    }

    companion object {
        fun start(context: Context, url: String) {
            val starter = Intent(context, DLNAActivity::class.java)
            starter.putExtra(Constant.ADS, url)
            context.startActivity(starter)
        }


    }

}