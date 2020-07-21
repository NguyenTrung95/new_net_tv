package com.eliving.tv.ui.launcher.nettv.activity

import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.lifecycle.Observer
import com.eliving.tv.R
import com.eliving.tv.base.activity.BaseVMActivity
import com.eliving.tv.base.adapter.OnItemClickListener
import com.eliving.tv.base.dialog.LoadingIndicator
import com.eliving.tv.config.Settings
import com.eliving.tv.service.live.viewmodel.LiveViewModel
import com.eliving.tv.service.user.model.response.LiveResponse
import com.eliving.tv.ui.channels.AdapterLive
import com.eliving.tv.ui.launcher.nettv.adapter.LiveAdapter
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.util.TrustCertificate
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.activity_full_screen_new.*
import kotlinx.android.synthetic.main.layout_media_controller_new.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LivePlayerActivity : BaseVMActivity(), View.OnClickListener,
        OnTouchListener, OnItemSelectedListener, AdapterView.OnItemClickListener {
    private var isLive = false
    private val liveViewModel: LiveViewModel by viewModel()
    private lateinit var adapter: AdapterLive
    private var datas: ArrayList<LiveResponse> = ArrayList()
    private var position = 0
    private var mPos = 0
    private var url = ""
    private val loadingDialog by lazy {
        LoadingIndicator(this)
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_full_screen_new
    }

    override fun initView() {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mPos = intent.getIntExtra("pos",0)
        url = intent.getStringExtra("url")
        li_live_list_layout.visibility = GONE
        mLiveListView.requestFocus()
        rlVideoPlayer!!.setOnTouchListener(this)


        imgPlay!!.setOnClickListener(this)
        imgZoomOut!!.setOnClickListener(this)
        isLive = true
        TrustCertificate.excute();
        getChannel()

    }

    fun getChannel() {

        Settings.Auth.isAuth = true

        liveViewModel.getLiveChannel().observe(this, Observer {
            val data = it
            val jsonArr: JsonArray = data.getAsJsonArray("data")
            jsonArr.map { jsonElement -> jsonElement.asJsonObject }.map {
                datas.add(LiveResponse(it))
            }
            Log.d("thinh", datas.size.toString())
            adapter = AdapterLive(object : OnItemClickListener<LiveResponse> {
                override fun onClick(item: LiveResponse, position: Int) {
//                initChannel(item.day?:"",channelsData?.get(channelsPos)?.id?:"30")
                }

            })
            mLiveListView.adapter = adapter
            adapter.setListItems(datas)
            mLiveListView.visibility = VISIBLE
            li_live_list_layout.visibility = GONE
            for (i in 0 until datas.size - 1) {
                datas[i].isSelected = false
            }
            position = mPos
            datas[mPos].isSelected = true
            mLiveListView.scrollToPosition(mPos)
            bindData(datas[mPos].stream_url)
            Handler().removeCallbacks(hideControllerRunnable)


        })

    }

    override fun getViewModel() = liveViewModel

    private fun bindData(mUrl: String?) {
        if (!isLive) {
            videoPlayer!!.useController = true
        }
        loadingDialog.show()
        videoPlayer!!.setVideoUri(mUrl, "")
        videoPlayer!!.player.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(
                    playWhenReady: Boolean,
                    playbackState: Int
            ) {
                if (playbackState == Player.STATE_READY) {
                    loadingDialog.hide()
                    videoPlayer!!.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        videoPlayer!!.player.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        videoPlayer!!.player.playWhenReady = false
        imgPlay!!.setImageResource(R.drawable.ic_play)
    }

    override fun onStop() {
        super.onStop()
        videoPlayer!!.player.playWhenReady = false
        imgPlay!!.setImageResource(R.drawable.ic_play)
    }

    override fun onBackPressed() {
        videoPlayer!!.player.release()
        super.onBackPressed()
    }

    override fun onRestart() {
        super.onRestart()
        videoPlayer!!.player.playWhenReady = true
    }

    override fun onDestroy() {
        super.onDestroy()
        videoPlayer!!.player.release()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.imgPlay -> if (videoPlayer != null) {
                isPausePlayVideo
                Handler().postDelayed(hideControllerRunnable, 2000)
            }
        }
    }

    private val isPausePlayVideo: Unit
        private get() {
            if (isPlaying) {
                imgPlay!!.setImageResource(R.drawable.ic_play)
                videoPlayer!!.player.playWhenReady = false
            } else {
                imgPlay!!.setImageResource(R.drawable.ic_pause)
                videoPlayer!!.player.playWhenReady = true
            }
            Handler().removeCallbacks(hideControllerRunnable)
        }

    private val hideControllerRunnable = Runnable { hideController() }

    private fun hideController() {
        mLiveListView.visibility = GONE
        li_live_list_layout!!.visibility = GONE
    }

    private fun showController() {
        mLiveListView.visibility = VISIBLE
        mLiveListView.requestFocus()
        li_live_list_layout!!.visibility = VISIBLE
    }

    private val isPlaying: Boolean
        private get() {
            if (videoPlayer != null) {
                if (videoPlayer!!.player != null) {
                    return videoPlayer!!.player != null && videoPlayer!!.player.playbackState != Player.STATE_ENDED && videoPlayer!!.player.playbackState != Player.STATE_IDLE && videoPlayer!!.player.playWhenReady
                }
            }
            return false
        }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        if (view.id == R.id.rlVideoPlayer) {
            if (li_live_list_layout!!.visibility == VISIBLE) {
                hideController()
            } else {
                showController()
                Handler().postDelayed(hideControllerRunnable, 2000)
            }
        }
        return false
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (VISIBLE !== 0) {
            return super.dispatchKeyEvent(event)
        }
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
                    return true
                }
                9 -> {
                    hideController()
                    return true
                }
                10 -> {
                    for (i in 0 until datas.size - 1) {
                        if(datas[i].isSelected){
                            position = i
                            break
                        }
                    }
                    mLiveListView.scrollToPosition(position)
                    showController()
                    return true
                }
                19 -> {
                    // giam kenh
                    for (i in 0 until datas.size - 1) {
                        if(datas[i].isSelected){
                            position = i
                            break
                        }
                    }
                    if (position > 0) position -= 1
                    else position = 0
                    for (i in 0 until datas.size - 1) {
                        datas[i].isSelected = false
                    }
                    datas[position].isSelected = true
                    mLiveListView.scrollToPosition(position)
//                    bindData(datas[position].stream_url)
                    adapter?.notifyDataSetChanged()

                    return true
                }
                87 -> {
                    // giam kenh
                    for (i in 0 until datas.size - 1) {
                        if(datas[i].isSelected){
                            position = i
                            break
                        }
                    }
                    if (position > 0) position -= 1
                    else position = 0
                    for (i in 0 until datas.size - 1) {
                        datas[i].isSelected = false
                    }
                    datas[position].isSelected = true
                    mLiveListView.scrollToPosition(position)
                    bindData(datas[position].stream_url)
                    adapter?.notifyDataSetChanged()

                    return true
                }
                20 -> {
                    // tang kenh
                    for (i in 0 until datas.size - 1) {
                        if(datas[i].isSelected){
                            position = i
                            break
                        }
                    }
                    if (position < datas.size - 1) position += 1
                    else position = datas.size - 1
                    for (i in 0 until datas.size - 1) {
                        datas[i].isSelected = false
                    }
                    datas[position].isSelected = true
                    mLiveListView.scrollToPosition(position)
                    Log.d("thinh - pos - key20", "${position}")
                    adapter?.notifyDataSetChanged()
                    return true
                }
                88 -> {
                    // tang kenh
                    for (i in 0 until datas.size - 1) {
                        if(datas[i].isSelected){
                            position = i
                            break
                        }
                    }
                    if (position < datas.size - 1) position += 1
                    else position = datas.size - 1
                    for (i in 0 until datas.size - 1) {
                        datas[i].isSelected = false
                    }
                    datas[position].isSelected = true
                    mLiveListView.scrollToPosition(position)
                    Log.d("thinh - pos - key20", "${position}")
                    bindData(datas[position].stream_url)
                    adapter?.notifyDataSetChanged()
                    return true
                }
//                19 -> {
//                    // giam kenh
//                    var position = 0
//                    for (i in 0 until datas.size - 1) {
//                        if(datas[i].isSelected){
//                            position = i
//                            break
//                        }
//                    }
//                    if(position == 0){
//                        for (i in 0 until datas.size - 1) {
//                            datas[i].isSelected = false
//                        }
//                        datas[datas.size - 1].isSelected = true
//                        bindData(datas[datas.size - 1].stream_url)
//                    }else{
//                        position = position.minus(1)
//                        for (i in 0 until datas.size - 1) {
//                            datas[i].isSelected = false
//                        }
//                        datas[position].isSelected = true
//                        bindData(datas[position].stream_url)
//                    }
//
//                    adapter?.notifyDataSetChanged()
//
//                    return true
//                }
//                20 -> {
//                    // tang kenh
//                    var position = 0
//                    for (i in 0 until datas.size - 1) {
//                        if(datas.get(i).isSelected == true){
//                            position = i
//                            break
//                        }
//                    }
//                    if(position == datas.size - 1){
//                        datas.get(0).isSelected = true
//                        bindData(datas.get(datas.size - 1).stream_url)
//                    }else{
//                        position = position.and(1)
//                        datas.get(position).isSelected = true
//                        bindData(datas.get(position).stream_url)
//                    }
//                    adapter?.notifyDataSetChanged()
//                    return true
//                }
                21 -> {
                 // trai
                    return true
                }
                22 -> {
                    // phai

                    return true
                }
                23 -> {
                    //ok
                    if (li_live_list_layout!!.visibility == VISIBLE) {
                        hideController()
                        bindData(datas[position].stream_url)

                    } else {
                        showController()
//                        Handler().postDelayed(hideControllerRunnable, 2000)
                    }
                    return true
                }
                82 -> {
                   // menu
                    showController()
                    return true
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        for (i in 0 until datas.size - 1) {
            datas.get(i).isSelected = false
        }
        datas.get(p2).isSelected = true
        adapter?.notifyDataSetChanged()
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        bindData(datas.get(p2).stream_url)
        Handler().removeCallbacks(hideControllerRunnable)
        adapter?.notifyDataSetChanged()

    }
}