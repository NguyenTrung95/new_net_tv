package com.nencer.nencerwallet.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.nencer.nencerwallet.base.viewmodel.BaseViewModel
import com.nencer.nencerwallet.base.viewmodel.ErrorState
import com.nencer.nencerwallet.base.viewmodel.LoadState
import com.nencer.nencerwallet.base.viewmodel.SuccessState
import com.nencer.nencerwallet.ext.errorToast

/**
 * FragmentBase with ViewModel
 */
abstract class BaseVMFragment : Fragment() {

    private var mRootView: View? = null

    private var mIsCreateView: Boolean = false

    private var mIsHasData: Boolean = false

    lateinit var mActivity: AppCompatActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModelAction()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mRootView == null) {
            mRootView = View.inflate(container?.context, getLayoutRes(), null)
        }
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mIsCreateView = true
        initView()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && mIsCreateView) {//可见并且view被创建后后才加载数据
            lazyLoadData()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {//防止和ViewPager结合使用时，第一个Fragment空白问题
        super.onActivityCreated(savedInstanceState)
        if (userVisibleHint) {//可见时才加载数据
            lazyLoadData()
        }
    }

    private fun lazyLoadData() {
        if (!mIsHasData) {
            mIsHasData = true
            initData()
        }
    }

    private fun initViewModelAction() {
        this.getViewModel().let { baseViewModel ->
            baseViewModel.mStateLiveData.observe(this, Observer { stateActionState ->
                when (stateActionState) {
                    LoadState -> showLoading()
                    SuccessState -> dismissLoading()
                    is ErrorState -> {
                        dismissLoading()
                        stateActionState.message?.apply {
                            errorToast(this)
                            handleError()
                        }
                    }
                }
            })
        }
    }

    abstract fun getLayoutRes(): Int

    abstract fun getViewModel(): BaseViewModel

    abstract fun initView()

    open fun initData() {

    }

    open fun showLoading() {

    }

    open fun dismissLoading() {

    }

    open fun handleError() {

    }
}