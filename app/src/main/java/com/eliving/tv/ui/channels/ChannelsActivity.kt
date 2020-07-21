package com.eliving.tv.ui.channels

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import com.eliving.tv.R
import com.eliving.tv.base.activity.BaseVMActivity
import com.eliving.tv.base.adapter.OnItemClickListener
import com.eliving.tv.base.dialog.LoadingIndicator
import com.eliving.tv.base.viewmodel.BaseViewModel
import com.eliving.tv.config.Settings
import com.eliving.tv.ext.GlideApp
import com.eliving.tv.ext.optJsonArray
import com.eliving.tv.helper.ToastUtil
import com.eliving.tv.service.live.model.response.*
import com.eliving.tv.service.live.viewmodel.LiveViewModel
import com.google.android.exoplayer2.util.TrustCertificate
import kotlinx.android.synthetic.main.activity_chanels.*
import kotlinx.android.synthetic.main.activity_full_screen_new.videoPlayer
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class ChannelsActivity : BaseVMActivity(),AdapterView.OnItemSelectedListener{

    private val viewModel: LiveViewModel by viewModel()
    private val loadingDialog by lazy {
        LoadingIndicator(this)
    }
    private var channelsData: ArrayList<ChannelsEntity> = arrayListOf()
    private var dates: ArrayList<DateEntity> = arrayListOf()
    private val channelsKey: MutableList<Channels> = mutableListOf()

    private lateinit var adapter: MoreVodAdapter
    private lateinit var dateTimeAdapter: DateTimeAdapter


    private var mDateInx = 0
    private var channelsPos = 0
    private var channelsDataPos = 0

    private var urlIntro = ""

    private var mTimer: Timer? = null

    /* access modifiers changed from: private */
    private var mUiHandler: UiHandler? = null

    private var isLive = false
    private var dateStr = ""
    private var timeStr = ""

    private var type = 0
    private var position = 0
    companion object {
        private var TAG = ChannelsActivity::class.java.name
        fun start(context: Context, type: Int) {
            val starter = Intent(context, ChannelsActivity::class.java)
            starter.putExtra(TAG, type)
            context.startActivity(starter)
        }


    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun getLayoutId(): Int = R.layout.activity_chanels
    override fun initView() {
        type = intent.getIntExtra(TAG, 0)

        loadingDialog.show()

        mUiHandler = UiHandler()
        mTimer = Timer()
        Settings.Auth.isAuth = true

        viewModel.getLiveChannel().observe(this, Observer {
            it.optJsonArray("data")?.let {
                val channels = ChannelsResponse(it)
                channelsKey.addAll(channels.datas)
                channelsPos = 0
                tv_channels_name.text = "${channelsKey[channelsPos].channel_number}  ${channelsKey[channelsPos].title}"
                GlideApp.with(this).load(channelsKey[channelsPos].icon_url).into(channelicon)

                dateTimeAdapter = DateTimeAdapter(object :OnItemClickListener<DateEntity>{
                    override fun onClick(item: DateEntity, position: Int) {
//                initChannel(item.day?:"",channelsData?.get(channelsPos)?.id?:"30")
                    }

                })
                rcyDate.requestFocus()
                rcyDate.adapter = dateTimeAdapter
                when(type){
                    1 ->{
                        getCatchupDate()
                    }
                    2 -> {
                        getEpgDate()
                    }

                }
            }
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
        }, 1000, 60000)

        isLive = true

        TrustCertificate.excute();
        rcy_channels.isFocusable = true

    }

    private fun getCatchupDate() {
        loadingDialog.show()
        viewModel.getCatchupDate().observe(this, Observer {
            dates.addAll(it.datas)
            dates[0].day?.let {
                if (channelsKey.size > 0) initChannelCatchup(it,channelsKey?.get(channelsPos)?.id?:"30",true)
                else  initChannelCatchup(it,"30",true)
            }
            Log.d("thinh", dates.size.toString())
            for (i in 0 until dates.size - 1) {
                dates[i].isSelected = false
            }
            position = 0
            dates[position].isSelected = true
            dateTimeAdapter.setListItems(dates)

//            rcyDate.requestFocus()
            rcyDate.scrollToPosition(position)
            ll_channels_name.requestFocus()
            ll_channels_name.isFocusable = true
            rcyDate.isFocusable = false
            dateTimeAdapter.notifyItemChanged(position)
//            dateTimeAdapter.notifyDataSetChanged()
            loadingDialog.hide()

        })
    }
    private fun getEpgDate() {
        loadingDialog.show()
        viewModel.getEpgDate().observe(this, Observer {
            dates.addAll(it.datas)
            dates[0].day?.let {
                if (channelsKey.size > 0) initChannelEpg(it,channelsKey?.get(channelsPos)?.id?:"30",true)
                else  initChannelEpg(it,"30",true)
            }
            Log.d("thinh", dates.size.toString())
            for (i in 0 until dates.size - 1) {
                dates[i].isSelected = false
            }
            position = 0
            dates[position].isSelected = true
            dateTimeAdapter.setListItems(dates)
            rcyDate.scrollToPosition(position)
            ll_channels_name.requestFocus()
            ll_channels_name.isFocusable = true
            rcyDate.isFocusable = false
            dateTimeAdapter.notifyItemChanged(position)
            loadingDialog.hide()

        })
    }
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {

        val keyCode = event.keyCode
        Log.d("thinh", "[keycode]$keyCode")
        Log.d("thinh", "[KeyEvent..Action]" + event.action)
        Toast.makeText(this, "keyCode == >" + keyCode , Toast.LENGTH_SHORT).show()

        if (event.action == 1) {
            when (keyCode) {
                183, 184, 186 -> return true
            }
        } else if (event.action == 0) {
            when (keyCode) {
                4 -> {
                    onBackPressed()
                }

                19 -> {
                    // len
                    if(rcyDate.hasFocus()){
                        ll_channels_name.isFocusableInTouchMode = true
                        ll_channels_name.isFocusable = true
                        rcyDate.clearFocus()
                    }

                    if(rcy_channels.hasFocus()){
                        if(type == 1){
                            if(channelsDataPos > 0 && channelsData.size > 0)
                                channelsDataPos -= 1
                            videoPlayer.setVideoUri(channelsData[channelsDataPos].stream_url , "")
                        }
                    }

                }
                20 -> {
                    // xuong
                    if(rcy_channels.hasFocus()){
                        if(type == 1){
                            if(channelsDataPos >= 0 && channelsDataPos < channelsData.size - 1 && channelsData.size > 0)
                                channelsDataPos += 1
                            videoPlayer.setVideoUri(channelsData[channelsDataPos].stream_url , "")
                        }
                    }
                }
                21 -> {
                    // trai

                    if(ll_channels_name.hasFocus()){
                        // reload lai data khi chon kenh
                        if(channelsPos > 0 && channelsPos < channelsKey.size){
                            channelsPos --
                            tv_channels_name.text = "${channelsKey[channelsPos].channel_number}  ${channelsKey[channelsPos].title}"
                            when(type){
                                1 ->{
                                    getCatchupDate()
                                }
                                2 -> {
                                    getEpgDate()
                                }
                            }
                        }

                    }

                    if(rcyDate.hasFocus()){
                        ll_channels_name.clearFocus()
                        // catchup
                        for (i in 0 until dates.size - 1) {
                            if(dates[i].isSelected){
                                position = i
                                break
                            }
                        }
                        if (position > 0) position -= 1
                        else position = 0
                        for (i in 0 until dates.size - 1) {
                            dates[i].isSelected = false
                        }
                        dates[position].isSelected = true
//                        dateTimeAdapter.setListItems(dates)
                        rcyDate.scrollToPosition(position)
                        dateTimeAdapter.notifyDataSetChanged()

                        when(type){
                            1 ->{
                                // call service lay danh sach catchup
                                dates[position].day?.let {
                                    if (channelsKey.size > 0) initChannelCatchup(it, channelsKey?.get(channelsPos)?.id ?: "30", false)
                                    else initChannelCatchup(it, "30",false)
                                }
                            }
                            2 -> {
                                // call service lay danh sach epg
                                dates[position].day?.let {
                                    if (channelsKey.size > 0) initChannelEpg(it, channelsKey?.get(channelsPos)?.id ?: "30",false)
                                    else initChannelEpg(it, "30",false)
                                }
                            }

                        }
                    }
                    return true
                }
                22 -> {
                    // phai
                    if(ll_channels_name.hasFocus()){
                        // reload lai data khi chon kenh
                        if(channelsPos >= 0 && channelsPos < channelsKey.size -1){
                            channelsPos ++
                            tv_channels_name.text = "${channelsKey[channelsPos].channel_number}  ${channelsKey[channelsPos].title}"
                            when(type){
                                1 ->{
                                    getCatchupDate()
                                }
                                2 -> {
                                    getEpgDate()
                                }
                            }
                        }

                    }

                    if(rcyDate.hasFocus()){
                        ll_channels_name.clearFocus()
                        // catchup
                        for (i in 0 until dates.size - 1) {
                            if(dates[i].isSelected){
                                position = i
                                break
                            }
                        }
                        if (position < dates.size - 1) position += 1
                        else position = dates.size - 1
                        for (i in 0 until dates.size - 1) {
                            dates[i].isSelected = false
                        }
                        dates[position].isSelected = true
//                        dateTimeAdapter.setListItems(dates)
                        rcyDate.scrollToPosition(position)
                        dateTimeAdapter.notifyDataSetChanged()
                        when(type){
                            1 ->{
                                // call service lay danh sach catchup
                                dates[position].day?.let {
                                    if (channelsKey.size > 0) initChannelCatchup(it, channelsKey?.get(channelsPos)?.id ?: "30", false)
                                    else initChannelCatchup(it, "30",false)
                                }
                            }
                            2 -> {
                                // call service lay danh sach epg
                                dates[position].day?.let {
                                    if (channelsKey.size > 0) initChannelEpg(it, channelsKey?.get(channelsPos)?.id ?: "30",false)
                                    else initChannelEpg(it, "30",false)
                                }
                            }

                        }
                    }
//                    ll_channels_name.isFocusableInTouchMode = false;
//                    ll_channels_name.isFocusable = false;
//                    rcyDate.requestFocus()
                }
                23 -> {
                    // ok
                    if(rcy_channels.hasFocus()){
                        var mPos = 0
                        for (i in 0 until channelsData.size - 1) {
                            if(channelsData[i].isSelected){
                                mPos = i
                                break
                            }
                        }
                        PlayerActivity.start(this@ChannelsActivity, channelsData[mPos].stream_url ?: "")

                    }

                    return true
                }
                82 -> {
                    // menu
                    return true
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }

    override fun initData() {
        adapter = MoreVodAdapter(object : OnItemClickListener<ChannelsEntity> {
            override fun onClick(item: ChannelsEntity, position: Int) {
                PlayerActivity.start(this@ChannelsActivity, item.stream_url ?: "")
            }
        })
        rcy_channels.adapter = adapter
        Settings.Auth.isAuth = true

    }

    private fun initChannelCatchup(date: String, channelsId: String, isFirst : Boolean) {
        ToastUtil.show(date)
        channelsDataPos = 0
        channelsData.clear()
        viewModel.getCatchup(channelsId, date).observe(this, Observer {

            loadingDialog.hide()
            val data = it.optJsonArray("data")
            data?.let {
                channelsData = CatchupResponse(data).datas
            }
            channelsDataPos = 0
            adapter.listItems.clear()
            if (channelsData.size > 0) {
                adapter.setListItems(channelsData)
                videoPlayer!!.setVideoUri(channelsData[channelsDataPos].stream_url, "")
            } else {
                adapter.setListItems(arrayListOf())
                videoPlayer!!.releasePlayer()


            }
            adapter.notifyDataSetChanged()
            if(!isFirst){
                ll_channels_name.clearFocus()
                rcyDate.requestFocus()
                rcyDate.isFocusable = true
            }
            loadingDialog.hide()

        })


    }

    private fun initChannelEpg(date: String, channelsId: String, isFirst : Boolean) {
        ToastUtil.show(date)

        channelsData.clear()
        viewModel.getEpg(channelsId, date).observe(this, Observer {

            loadingDialog.hide()
            val data = it.optJsonArray("data")
            data?.let {
                channelsData = CatchupResponse(data).datas
            }

            adapter.listItems.clear()
            if (channelsData.size > 0) {
//                videoPlayer!!.setVideoUri(channelsData[0].stream_url, "")
                adapter.setListItems(channelsData)
            } else {
                adapter.setListItems(arrayListOf())

            }
            adapter.notifyDataSetChanged()
            if(!isFirst){
                ll_channels_name.clearFocus()
                rcyDate.requestFocus()
                rcyDate.isFocusable = true
            }
            loadingDialog.hide()

        })

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

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
    }




}



