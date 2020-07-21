package com.eliving.tv.ui.launcher.nettv.fragment

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import com.eliving.tv.R
import com.eliving.tv.base.fragment.BaseVMFragment
import com.eliving.tv.base.viewmodel.BaseViewModel
import com.eliving.tv.service.live.viewmodel.LiveViewModel
import com.eliving.tv.ui.ads.ADActivity
import com.eliving.tv.ui.channels.ChanelType
import com.eliving.tv.ui.channels.ChannelsActivity
import com.eliving.tv.ui.launcher.nettv.activity.LiveCategoryActivity
import com.eliving.tv.ui.launcher.nettv.activity.LivePlayerActivity
import com.eliving.tv.ui.radio.RadioActivity
import com.eliving.tv.ui.vod.VodActivity
import kotlinx.android.synthetic.main.fragment_tv.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TvFragment : BaseVMFragment(), OnFocusChangeListener {
    private val userModel : LiveViewModel by viewModel()
    private var isFirst = true



    override fun getLayoutRes(): Int {
        return R.layout.fragment_tv;
    }

    override fun getViewModel(): BaseViewModel = userModel


    override fun initView() {
        ad1.onFocusChangeListener = this
        ad1.requestFocus()
        ad1.setOnClickListener{
            val intent = Intent(activity, LiveCategoryActivity::class.java)
            startActivity(intent)
        }
        ll_catchup.onFocusChangeListener = this
        ll_catchup.setOnClickListener {
            ChannelsActivity.start(activity!!,1)
        }
        ll_epg.onFocusChangeListener = this
        ll_epg.setOnClickListener {
            ChannelsActivity.start(activity!!,2)

        }
        ll_dowload.onFocusChangeListener = this
        ll_dowload.setOnClickListener {
            VodActivity.start(context!!)
        }
        rl_radio.onFocusChangeListener = this
        rl_radio.setOnClickListener {
            val intent = Intent(activity, RadioActivity::class.java)
            startActivity(intent)
        }
        rl_ads.onFocusChangeListener = this
        rl_ads.setOnClickListener {
            ADActivity.start(activity!!,"")
        }
    }

    override fun onFocusChange(view: View?, focused: Boolean) {
        Log.d("this.first", "${isFirst}")
        if (this.isFirst) {
            this.isFirst = false
            ad1.requestFocus()
            return
        }
        val toX = (view!!.width + 20).toFloat() / view!!.width.toFloat()
        val toY = (view!!.height + 20).toFloat() / view!!.height.toFloat()
        if (focused) {
            view!!.bringToFront()
            view!!.animate().scaleX(toX).scaleY(toY).setDuration(300).start()
            return
        }
        view!!.animate().scaleX(1.0f).scaleY(1.0f).setDuration(300).start()
    }
}