package com.nencer.nencerwallet.ui.main.news

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.nencer.nencerwallet.R
import com.nencer.nencerwallet.base.adapter.OnItemClickListener
import com.nencer.nencerwallet.base.fragment.BaseVMFragment
import com.nencer.nencerwallet.base.viewmodel.BaseViewModel
import com.nencer.nencerwallet.customview.CustomLoadingListItemCreator
import com.nencer.nencerwallet.ext.gone
import com.nencer.nencerwallet.ext.visible
import com.nencer.nencerwallet.service.info.InfoViewModel
import com.nencer.nencerwallet.service.info.model.NewsData
import com.nencer.nencerwallet.main.NewsData.NewsAdapter
import com.paginate.Paginate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_newst.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : BaseVMFragment(), Paginate.Callbacks {

    private val mViewModel: InfoViewModel by viewModel()
    private var adapter: NewsAdapter? = null
    private var page = 1
    private var loading: Boolean = false
    private var hasLoadedAllItems = false
    private var newsList: MutableList<NewsData> = mutableListOf()
    override fun getViewModel(): BaseViewModel = mViewModel

    override fun getLayoutRes(): Int = R.layout.fragment_newst

    override fun initView() {
        loading = false

        adapter = NewsAdapter(object : OnItemClickListener<NewsData> {
            override fun onClick(item: NewsData, position: Int) {
                NewsDetailActivity.start(
                    context!!,
                    item.id ?: 0
                )
            }

        })

        rcyNews.adapter = adapter
        rcyNews.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        loadData(false)

        Paginate.with(rcyNews, this)
            .setLoadingTriggerThreshold(2)
            .addLoadingListItem(true)
            .setLoadingListItemCreator(CustomLoadingListItemCreator())
            .build()


    }

    private fun loadData(loadMore: Boolean) {


        if (loading) {
            return
        }

        loading = true

        if (!loadMore) {
            page = 1
        }

        mViewModel.getlistnews(page).observe(this, Observer {
            loading = false
            hasLoadedAllItems = it.data?.size!! < 10
            newsList.addAll(it.data ?: mutableListOf())
            adapter?.setListItems(newsList)
            page += 1

        })

    }

    override fun onLoadMore() {
        loadData(true)
    }

    override fun isLoading(): Boolean = loading

    override fun hasLoadedAllItems(): Boolean = hasLoadedAllItems


}