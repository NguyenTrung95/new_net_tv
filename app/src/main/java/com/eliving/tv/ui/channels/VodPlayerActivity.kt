package com.eliving.tv.ui.channels

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.view.View.*
import android.view.WindowManager
import com.eliving.tv.R
import com.eliving.tv.base.activity.BaseActivity
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.util.TrustCertificate
import kotlinx.android.synthetic.main.activity_full_screen_new.*
import kotlinx.android.synthetic.main.layout_media_controller_new.*

class VodPlayerActivity : BaseActivity(), View.OnClickListener{
    private var isLive = false
    private var livePosition = 0

    companion object {
        private var TAG = VodPlayerActivity::class.java.name
        fun start(context : Context,url : String){
            val starter = Intent(context, VodPlayerActivity::class.java)
            starter.putExtra(TAG,url)
            context.startActivity(starter)
        }


    }

    override fun getLayoutId(): Int {
        return R.layout.activity_full_screen_new
    }

    override fun initView() {
        val url = intent.getStringExtra(TAG)

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_full_screen_new)
        li_live_list_layout.visibility = GONE
        mLiveListView.requestFocus()

        imgPlay!!.setOnClickListener(this)
        imgZoomOut!!.setOnClickListener(this)
        isLive = false
        TrustCertificate.excute();
        bindData(url)

    }

    private fun bindData(mUrl : String?) {
        if (!isLive) {
            videoPlayer!!.useController = true
        }
        loadingView!!.visibility = View.VISIBLE
        videoPlayer!!.setVideoUri(mUrl, "")
        videoPlayer!!.player.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(
                playWhenReady: Boolean,
                playbackState: Int
            ) {
                if (playbackState == Player.STATE_READY) {
                    loadingView!!.visibility = View.GONE
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
        li_live_list_layout!!.visibility = View.GONE
    }

    private fun showController() {
        li_live_list_layout!!.visibility = View.VISIBLE
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


    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (VISIBLE !== 0) {
            return super.dispatchKeyEvent(event)
        }
        val keyCode = event.keyCode

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
                19 -> {

                    return true
                }
                20 -> {

                    return true
                }
                21 -> {

                    return true
                }
                22, 184 -> {

                    return true
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }
}