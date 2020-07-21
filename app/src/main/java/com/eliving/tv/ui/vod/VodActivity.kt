package com.eliving.tv.ui.vod

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import androidx.lifecycle.Observer
import com.eliving.tv.R
import com.eliving.tv.base.activity.BaseVMActivity
import com.eliving.tv.base.adapter.OnItemClickListener
import com.eliving.tv.base.dialog.LoadingIndicator
import com.eliving.tv.base.viewmodel.BaseViewModel
import com.eliving.tv.config.Settings
import com.eliving.tv.service.live.model.response.*
import com.eliving.tv.service.vod.viewmodel.VODViewModel
import com.eliving.tv.ui.channels.ChanelType
import com.eliving.tv.ui.channels.DateTimeAdapter
import com.eliving.tv.ui.channels.VodPlayerActivity
import com.eliving.tv.ui.channels.VodAdapter
import com.google.android.exoplayer2.util.TrustCertificate
import kotlinx.android.synthetic.main.activity_chanels.*
import kotlinx.android.synthetic.main.activity_full_screen_new.videoPlayer
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class VodActivity : BaseVMActivity() {

    private val viewModel: VODViewModel by viewModel()
    private val loadingDialog by lazy {
        LoadingIndicator(this)
    }


    private lateinit var adapter : VodAdapter
    private lateinit var dateTimeAdapter: DateTimeAdapter

    private var mTimer: Timer? = null

    /* access modifiers changed from: private */
    private var mUiHandler: UiHandler? = null

    private var isLive = false
    private var dateStr = ""
    private var timeStr = ""
    companion object {
        private var TAG = VodActivity::class.java.name
        fun start(context: Context) {
            val starter = Intent(context, VodActivity::class.java)
            context.startActivity(starter)
        }


    }
    override fun getViewModel(): BaseViewModel = viewModel

    override fun getLayoutId(): Int = R.layout.activity_chanels
    override fun initView() {

        loadingDialog.show()

        mUiHandler = UiHandler()
        mTimer = Timer()

        /*rcyDate.adapter = dateTimeAdapter*/

        viewModel.category().observe(this, Observer {
            val a = it
            loadingDialog.hide()

        })

        viewModel.vod("1",1).observe(this, Observer {
           val a = it
        })
        mTimer?.schedule(object : TimerTask() {
            override fun run() {
                val calendar = Calendar.getInstance();
                val month = calendar[2] + 1
                val day = calendar[5]
                calendar[7]
                val hour = calendar[11]
                val minute = calendar[12]
                dateStr = "$month 月 $day 日"

                var hourStr = ""
                if (hour.toString().length == 1) hourStr = "0$hour"
                else hourStr = hour.toString()
                var minStr = ""
                if (minute.toString().length == 1) minStr = "0$minute"
                else minStr = minute.toString()
                timeStr = "$hourStr:$minStr"
                mUiHandler?.sendEmptyMessage(1);

            }
        },1000,60000)

        isLive = true

        TrustCertificate.excute();
        rcy_channels.isFocusable = true

    }

    override fun initData() {
        /*adapter = VodAdapter(object : OnItemClickListener<ChannelsEntity> {
                    override fun onClick(item: ChannelsEntity, position: Int) {
                        VodPlayerActivity.start(this@VodActivity,item.stream_url?:"")
                    }
                })
        rcy_channels.adapter = adapter
        Settings.Auth.isAuth = true
*/
    }


    override fun handleError() {

        loadingDialog.hide()
    }

    override fun onResume() {
        super.onResume()
        videoPlayer!!.player.playWhenReady = true
    }


    override fun onBackPressed() {
        videoPlayer!!.player.release()
        mTimer?.cancel()
        super.onBackPressed()
    }

    override fun onRestart() {
        super.onRestart()
        videoPlayer!!.player.playWhenReady = true
    }

    override fun onDestroy() {
        super.onDestroy()
        videoPlayer!!.player.release()
        mTimer?.cancel()
    }


    override fun onPause() {
        super.onPause()
        mTimer?.cancel()

    }


    @SuppressLint("HandlerLeak")
    inner class UiHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                1 -> {
                    date_pzb.text = dateStr
                    time_pzb.text = timeStr
                    return
                }

                else -> return
            }
        }
    }
}



