package com.eliving.tv.ui.launcher.nettv.activity

import androidx.fragment.app.FragmentTransaction
import com.eliving.tv.R
import com.eliving.tv.base.activity.BaseVMActivity
import com.eliving.tv.base.viewmodel.BaseViewModel
import com.eliving.tv.service.user.viewmodel.UserViewModel
import com.eliving.tv.ui.launcher.nettv.fragment.TvFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class NettvActivity : BaseVMActivity() {
    var TAG  = NettvActivity::class.java.canonicalName as String
    private val userModel : UserViewModel by viewModel()
    private val fragmentTransaction  : FragmentTransaction by lazy {
        supportFragmentManager.beginTransaction()
    }


    override fun getLayoutId(): Int {
        return R.layout.p2p;
    }

    override fun initView() {
        fragmentTransaction.add(R.id.contentLayout, TvFragment())
        fragmentTransaction.commit()
    }

    override fun initData() {
        super.initData()
    }

    override fun getViewModel(): BaseViewModel = userModel


    override fun onBackPressed() {
        super.onBackPressed()
    }

}