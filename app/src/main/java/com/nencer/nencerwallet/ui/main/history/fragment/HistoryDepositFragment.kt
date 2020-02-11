package com.nencer.nencerwallet.ui.main.history.fragment

import com.nencer.nencerwallet.R
import com.nencer.nencerwallet.base.fragment.BaseVMFragment
import com.nencer.nencerwallet.base.viewmodel.BaseViewModel
import com.nencer.nencerwallet.service.wallet.WalletViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryDepositFragment : BaseVMFragment(){
    private val mViewModel: WalletViewModel by viewModel()

    override fun getLayoutRes(): Int = R.layout.fragment_history_deposit

    override fun getViewModel(): BaseViewModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}