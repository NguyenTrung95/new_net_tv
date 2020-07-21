package com.eliving.tv.customview

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.eliving.tv.R


class CustomResDialog(private val mContext: Context, theme: Int) :
    Dialog(mContext, theme) {
    private var mBtnResCancel: Button? = null
    private var mBtnResOk: Button? = null
    private var mFlResBg: FrameLayout? = null
    private var mTvResContent: TextView? = null
    private fun initUi() {
        mFlResBg = findViewById<View>(R.id.fl_resdialog_bg) as FrameLayout
        mTvResContent = findViewById<View>(R.id.tv_res_content) as TextView
        mBtnResOk = findViewById<View>(R.id.btn_res_ok) as Button
        mBtnResCancel =
            findViewById<View>(R.id.btn_res_cancel) as Button
        val dialogWindow = window
        val lp = dialogWindow!!.attributes
        dialogWindow.setGravity(49)
        lp.y = 200
        dialogWindow.attributes = lp
    }

    fun setDialogBg(bgResource: Int) {
        mFlResBg!!.setBackgroundResource(bgResource)
    }

    fun setTextContent(text: String?) {
        mTvResContent!!.text = text
    }

    fun setOkBgResource(bgResource: Int) {
        mBtnResOk!!.setBackgroundResource(bgResource)
    }

    fun setCancelBgResource(bgResource: Int) {
        mBtnResCancel!!.setBackgroundResource(bgResource)
    }

    fun setOkClickListener(listener: View.OnClickListener?) {
        mBtnResOk!!.setOnClickListener(listener)
    }

    fun setCancelClickListener(listener: View.OnClickListener?) {
        mBtnResCancel!!.setOnClickListener(listener)
    }

    override fun show() {
        super.show()
        mBtnResOk!!.requestFocus()
    }

    init {
        setContentView(R.layout.reservation_dialog)
        initUi()
    }
}