package com.eliving.tv.customview

import android.app.Dialog
import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.eliving.tv.R

class CustomInfoDialog : Dialog {
    private val mBtnInfoCancel: Button? = null
    private var mContext: Context
    private var mCustomKey = false
    private var mFlResBg: FrameLayout? = null
    private var mKeyCode = 0
    private var mTvResContent: TextView? = null

    constructor(context: Context, theme: Int) : super(context, theme) {
        mContext = context
        setContentView(R.layout.info_dialog)
        initUi()
    }

    constructor(
        context: Context,
        theme: Int,
        customKey: Boolean,
        keyCode: Int
    ) : super(context, theme) {
        mContext = context
        mCustomKey = customKey
        mKeyCode = keyCode
        setContentView(R.layout.info_dialog)
        initUi()
    }

    private fun initUi() {
        mFlResBg = findViewById<View>(R.id.fl_infodialog) as FrameLayout
        mTvResContent = findViewById<View>(R.id.tv_infodialog) as TextView
        setBtnCancelClickListener(null)
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

    fun setBtnCancelBgResource(bgResource: Int) {
        if (mBtnInfoCancel != null) {
            mBtnInfoCancel.setBackgroundResource(bgResource)
        }
    }

    private fun setBtnCancelClickListener(listener: View.OnClickListener?) {
        if (mBtnInfoCancel != null) {
            mBtnInfoCancel.setOnClickListener(View.OnClickListener { dismiss() })
        }
    }

    override fun show() {
        super.show()
        if (mBtnInfoCancel != null) {
            mBtnInfoCancel.requestFocus()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (!mCustomKey || keyCode != mKeyCode) {
            return super.onKeyDown(keyCode, event)
        }
        dismiss()
        return false
    }
}
