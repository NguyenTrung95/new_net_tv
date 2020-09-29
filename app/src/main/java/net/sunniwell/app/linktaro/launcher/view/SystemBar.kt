package net.sunniwell.app.linktaro.launcher.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Canvas
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import net.sunniwell.app.linktaro.R
import net.sunniwell.common.tools.DateTime
import java.text.SimpleDateFormat
import java.util.*

class SystemBar(private val mContext: Context, attrs: AttributeSet?) :
    LinearLayout(mContext, attrs) {
    private var mDate: TextView? = null
    private var mNetworkReceiver: BroadcastReceiver? = null

    /* access modifiers changed from: private */
    var mNetworkStatus: ImageView? = null
    private var mTime: TextView? = null

    /* access modifiers changed from: private */
    var mUIHandler: Handler? = null

    private inner class NetworkChangeReceiver private constructor() : BroadcastReceiver() {
        /* synthetic */
        internal constructor(
            systemBar: SystemBar?,
            networkChangeReceiver: NetworkChangeReceiver?
        ) : this() {
        }

        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            if (!intent.getBooleanExtra("noConnectivity", false)) {
                val cm =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (cm != null) {
                    val networkInfo = cm.activeNetworkInfo
                    if (networkInfo != null && networkInfo.isAvailable) {
                        mUIHandler!!.sendEmptyMessage(1)
                        return
                    }
                    return
                }
                return
            }
            mUIHandler!!.sendEmptyMessage(2)
        }
    }

    private inner class UIHandler(looper: Looper?) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                1 -> {
                    mNetworkStatus!!.setBackgroundResource(R.drawable.networklogo_line)
                    return
                }
                2 -> {
                    mNetworkStatus!!.setBackgroundResource(R.drawable.networklogo)
                    return
                }
                3 -> {
                    updateTime()
                    mUIHandler!!.sendEmptyMessageDelayed(
                        3,
                        DateTime.MILLIS_PER_MINUTE
                    )
                    return
                }
                else -> return
            }
        }
    }

    /* access modifiers changed from: protected */
    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mContext.registerReceiver(
            mNetworkReceiver,
            IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        )
    }

    /* access modifiers changed from: protected */
    public override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    /* access modifiers changed from: protected */
    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mContext.unregisterReceiver(mNetworkReceiver)
    }

    /* access modifiers changed from: private */
    fun updateTime() {
        val date = Date(System.currentTimeMillis())
        mDate!!.text = SimpleDateFormat("MM月dd日(EEEE)", Locale.getDefault()).format(date)
        mTime!!.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
    }

    companion object {
        private const val MSG_DATE = 3
        private const val MSG_NETWORK_CONNECT = 1
        private const val MSG_NETWORK_DISCONNECT = 2
    }

    init {
        (mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.bar_info,
            this
        )
        if (!isInEditMode) {
            mNetworkStatus =
                findViewById<View>(R.id.net_status) as ImageView
            mDate = findViewById<View>(R.id.system_bar_date) as TextView
            mTime = findViewById<View>(R.id.system_bar_time) as TextView
            mNetworkReceiver = NetworkChangeReceiver(this, null)
            mUIHandler = UIHandler(mContext.mainLooper)
            (mUIHandler as UIHandler).sendEmptyMessage(3)
        }
    }
}