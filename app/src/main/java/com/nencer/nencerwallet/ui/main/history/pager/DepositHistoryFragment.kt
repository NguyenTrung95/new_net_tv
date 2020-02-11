package com.nencer.nencerwallet.ui.main.history.pager

import androidx.lifecycle.Observer
import com.nencer.nencerwallet.R
import com.nencer.nencerwallet.base.adapter.OnItemClickListener
import com.nencer.nencerwallet.base.fragment.BaseVMFragment
import com.nencer.nencerwallet.base.viewmodel.BaseViewModel
import com.nencer.nencerwallet.customview.CustomLoadingListItemCreator
import com.nencer.nencerwallet.ext.gone
import com.nencer.nencerwallet.ext.visible
import com.nencer.nencerwallet.service.payment.PayViewModel
import com.nencer.nencerwallet.service.wallet.WalletViewModel
import com.nencer.nencerwallet.service.wallet.response.HistoryInfo
import com.nencer.nencerwallet.ui.main.history.adapter.wallet.HistoryDepositAdapter
import com.nencer.nencerwallet.ui.wallet.DepositDetailActivity
import com.paginate.Paginate
import kotlinx.android.synthetic.main.fragment_pager.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class DepositHistoryFragment : BaseVMFragment(), Paginate.Callbacks {


    private val mViewModel: PayViewModel by viewModel()
    private var page = 1
    private var loading: Boolean = false
    private var hasLoadedAllItems = false

    private lateinit var adapterDeposit: HistoryDepositAdapter
    private val listHistoryCommon: MutableList<HistoryInfo> = mutableListOf()
    private val mWalletDepositModel: WalletViewModel by viewModel()


    override fun getViewModel(): BaseViewModel = mViewModel

    override fun getLayoutRes(): Int = R.layout.fragment_pager

    override fun initData() {
        loading = false

        adapterDeposit =
            HistoryDepositAdapter(
                object :
                    OnItemClickListener<HistoryInfo> {
                    override fun onClick(item: HistoryInfo, position: Int) {

                        DepositDetailActivity.start(context!!,don_hang = item.order_code?:"",
                            vi_nhan = item.payee_wallet,so_tien = item.net_amount.toString(),phi = item.fees.toString()
                            ,hinh_thuc = item.bank_code,
                            ngay_tao = item.created_at,trang_thai = item.status,chu_tk = item.bankinfo?.account_name?:""
                            ,stk = item.bankinfo?.account_number?:"",so_atm = item.bankinfo?.account_card_number?:"",
                            chi_nhanh = item.bankinfo?.account_branch?:""
                            ,noi_dung_chuyen = item.description,so_tien_chuyen = item.pay_amount.toString(), bank_name = "", isDeposit = true)
                    }
                })
        rcyPager.adapter = adapterDeposit

        loadDataHistoryWallet(false)

        Paginate.with(rcyPager, this)
            .setLoadingTriggerThreshold(2)
            .addLoadingListItem(true)
            .setLoadingListItemCreator(CustomLoadingListItemCreator())
            .build()
    }

    override fun onResume() {
        super.onResume()
//        initData()
    }

    override fun initView() {



        swr_pager.setOnRefreshListener {
            listHistoryCommon.clear()
            loadDataHistoryWallet(false)
        }

    }


    private fun loadDataHistoryWallet(loadMore: Boolean) {
        if (loading) {
            return
        }

        loading = true

        if (!loadMore) {
            page = 1
        }



        if (!loadMore) {
            page = 1
        }

        mWalletDepositModel.history("Deposit", page).observe(
            this,
            Observer { historyResponse ->
                loading = false
                swr_pager.isRefreshing = false
                hasLoadedAllItems = historyResponse.data.size < 10
                listHistoryCommon.addAll(historyResponse.data)
                adapterDeposit.setListItems(listHistoryCommon)
                page += 1
                if (listHistoryCommon.size == 0) {
                    tv_status.visible()
                    rcyPager.gone()
                } else {
                    tv_status.gone()
                    rcyPager.visible()
                }
            })


    }


    override fun onLoadMore() {
        loadDataHistoryWallet(true)

    }

    override fun isLoading(): Boolean = loading

    override fun hasLoadedAllItems(): Boolean = hasLoadedAllItems

}