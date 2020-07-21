package com.eliving.tv.ui.ads

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

class ADActivity : Activity() {
    private var mImageView: ImageView? = null

    /* access modifiers changed from: protected */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad)
        mImageView =
            findViewById<View>(R.id.imageView) as ImageView
    }

    companion object {
        fun start(context: Context, url: String) {
            val starter = Intent(context, ADActivity::class.java)
            starter.putExtra(Constant.ADS, url)
            context.startActivity(starter)
        }


    }
    /* access modifiers changed from: protected */
    public override fun onResume() {
        super.onResume()
        val url = intent.getStringExtra(Constant.ADS)
        if (url != null) {
            Glide.with((this as Activity)).load(url).placeholder(R.color.black)
                .error(R.drawable.launcher_detil).into(mImageView!!)
        }
    }
}