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
import com.nencer.nencerwallet.ui.main.history.adapter.wallet.HistoryAllAdapter
import com.paginate.Paginate
import kotlinx.android.synthetic.main.fragment_pager.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class AllHistoryFragment : BaseVMFragment(), Paginate.Callbacks{


    private val mViewModel: PayViewModel by viewModel()
    private var page = 1
    private var loading: Boolean = false
    private var hasLoadedAllItems = false

    private lateinit var adapter: HistoryAllAdapter

    private val listHistoryCommon: MutableList<HistoryInfo> = mutableListOf()

    private val mWalletDepositModel: WalletViewModel by viewModel()


    override fun getViewModel(): BaseViewModel = mViewModel

    override fun getLayoutRes(): Int = R.layout.fragment_pager

    override fun initData() {
        loading = false

        adapter =
            HistoryAllAdapter(
                object :
                    OnItemClickListener<HistoryInfo> {
                    override fun onClick(item: HistoryInfo, position: Int) {
                    }
                })
        rcyPager.adapter = adapter

        loadDataHistoryWallet(false)

        Paginate.with(rcyPager, this)
            .setLoadingTriggerThreshold(2)
            .addLoadingListItem(true)
            .setLoadingListItemCreator(CustomLoadingListItemCreator())
            .build()
    }

    override fun initView() {


        swr_pager.setOnRefreshListener {
            listHistoryCommon.clear()
            loadDataHistoryWallet(false)
        }


    }

    override fun onResume() {
        super.onResume()
//        loadDataHistoryWallet(false)
    }


    private fun loadDataHistoryWallet(loadMore: Boolean) {
        if (loading) {
            return
        }

        loading = true

        if (!loadMore) {
            page = 1
        }
                mWalletDepositModel.history("All", page).observe(
                    this,
                    Observer { historyResponse ->
                        loading = false
                        swr_pager.isRefreshing = false
                        hasLoadedAllItems = historyResponse.data.size < 10
                        listHistoryCommon.addAll(historyResponse.data)
                        adapter.setListItems(listHistoryCommon)
                        page += 1
                        if(listHistoryCommon.size == 0) {
                            tv_status.visible()
                            rcyPager.gone()
                        }
                        else {
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