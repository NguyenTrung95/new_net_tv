package com.nencer.nencerwallet.main.NewsData

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nencer.nencerwallet.R
import com.nencer.nencerwallet.base.adapter.BaseAdapter
import com.nencer.nencerwallet.base.adapter.BaseViewHolder
import com.nencer.nencerwallet.base.adapter.OnItemClickListener
import com.nencer.nencerwallet.service.info.model.NewsData
import kotlinx.android.synthetic.main.item_news.view.*

class NewsAdapter(listener: OnItemClickListener<NewsData>): BaseAdapter<NewsData>(listener) {
    override fun getLayoutId(viewType: Int): Int = R.layout.item_news

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<NewsData> {
        return HomeViewHolder(view,listItems, mOnItemClickListener!!)
    }

    inner class HomeViewHolder(itemView: View, listItem: MutableList<NewsData>, listener: OnItemClickListener<NewsData>): BaseViewHolder<NewsData>(itemView,listItem,listener) {
        override fun bindData(t: NewsData, position: Int, viewType: Int) {

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(
                    RequestOptions()
                    .placeholder(R.drawable.ic_news)
                    .error(R.drawable.ic_news))
                .load("https://thedientu.com${t?.image?.trim()}")
                .into(itemView.imvThumbnail)
            itemView.tvTitle.text = t?.title
            itemView.tvDate.text = t?.created_at
            itemView.tvView.text = "Lượt xem: ${t?.view_count}"

        }
    }


}