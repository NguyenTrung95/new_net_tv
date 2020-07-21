/*
 * Copyright (C) 2016 hejunlin <hejunlin2013@gmail.com>
 * Github:https://github.com/hejunlin2013/TVSample
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eliving.tv.customview.recyclerviewfocus

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.*
import com.eliving.tv.customview.recyclerviewfocus.MetroViewBorderImpl

/**
 * Created by hejunlin on 2016/9/16.
 * blog: http://blog.csdn.net/hejjunlin
 */
class MetroViewBorderImpl<X : View?> : OnGlobalFocusChangeListener,
    OnScrollChangedListener, OnGlobalLayoutListener, OnTouchModeChangeListener {
    private var mViewGroup: ViewGroup? = null
    private var mMetroViewBorder: IMetroViewBorder? = null
    var view: X? = null
        private set
    private var mLastView: View? = null

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) {
        mMetroViewBorder = MetroViewBorderHandler()
        view = View(context, attrs, defStyleAttr) as X
    }

    constructor(view: X) {
        this.view = view
        mMetroViewBorder = MetroViewBorderHandler()
    }

    constructor(view: X, border: IMetroViewBorder?) {
        this.view = view
        mMetroViewBorder = border
    }

    constructor(context: Context?, resId: Int) : this(
        LayoutInflater.from(context).inflate(resId, null, false) as X
    ) {
    }

    fun setBackgroundResource(resId: Int) {
        if (view != null) view?.setBackgroundResource(resId)
    }

    override fun onScrollChanged() {
        mMetroViewBorder!!.onScrollChanged(view, mViewGroup)
    }

    override fun onGlobalLayout() {
        mMetroViewBorder!!.onLayout(view, mViewGroup)
    }

    override fun onTouchModeChanged(isInTouchMode: Boolean) {
        mMetroViewBorder!!.onTouchModeChanged(view, mViewGroup, isInTouchMode)
    }

    override fun onGlobalFocusChanged(
        oldFocus: View,
        newFocus: View
    ) {
        var oldFocus: View? = oldFocus
        try {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) { // 4.3
                if (oldFocus == null && mLastView != null) {
                    oldFocus = mLastView
                }
            }
            if (mMetroViewBorder != null) mMetroViewBorder!!.onFocusChanged(
                view,
                oldFocus,
                newFocus
            )
            mLastView = newFocus
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun <T : MetroViewBorderHandler?> getViewBorder(): T? {
        return mMetroViewBorder as T?
    }

    fun setBorder(border: IMetroViewBorder?) {
        mMetroViewBorder = border
    }

    fun attachTo(viewGroup: ViewGroup?) {
        var viewGroup = viewGroup
        try {
            if (viewGroup == null) {
                if (view!!.context is Activity) {
                    val activity = view!!.context as Activity
                    viewGroup =
                        activity.window.decorView.rootView as ViewGroup //获取顶层view
                }
            }
            if (mViewGroup !== viewGroup) {
                val viewTreeObserver = viewGroup!!.viewTreeObserver
                if (viewTreeObserver.isAlive && mViewGroup == null) {
                    viewTreeObserver.addOnGlobalFocusChangeListener(this)
                    viewTreeObserver.addOnScrollChangedListener(this)
                    viewTreeObserver.addOnGlobalLayoutListener(this)
                    viewTreeObserver.addOnTouchModeChangeListener(this)
                }
                mViewGroup = viewGroup
            }
            mMetroViewBorder!!.onAttach(view, viewGroup)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun detach() {
        detachFrom(mViewGroup)
    }

    fun detachFrom(viewGroup: ViewGroup?) {
        try {
            if (viewGroup === mViewGroup) {
                val viewTreeObserver =
                    mViewGroup!!.viewTreeObserver //获取view树的观察者
                viewTreeObserver.removeOnGlobalFocusChangeListener(this) //通知全局性移除相应的listener
                viewTreeObserver.removeOnScrollChangedListener(this)
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                viewTreeObserver.removeOnTouchModeChangeListener(this)
                mMetroViewBorder!!.OnDetach(view, viewGroup)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    companion object {
        private val TAG = MetroViewBorderImpl::class.java.simpleName
    }
}