package com.nencer.nencerwallet.ui.main.home.adapter

import android.view.View
import com.nencer.nencerwallet.R
import com.nencer.nencerwallet.base.adapter.BaseAdapter
import com.nencer.nencerwallet.base.adapter.BaseViewHolder
import com.nencer.nencerwallet.base.adapter.OnItemClickListener
import com.nencer.nencerwallet.ext.GlideApp
import com.nencer.nencerwallet.ext.placeholder
import com.nencer.nencerwallet.ui.main.home.model.Item
import kotlinx.android.synthetic.main.item_pay.view.*

class CardAdapter(listener: OnItemClickListener<Item>): BaseAdapter<Item>(listener) {
    override fun getLayoutId(viewType: Int): Int = R.layout.item_pay

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<Item> {
        return HomeViewHolder(view,listItems, mOnItemClickListener!!)
    }

    inner class HomeViewHolder(itemView: View, listItem: MutableList<Item>, listener: OnItemClickListener<Item>): BaseViewHolder<Item>(itemView,listItem,listener) {
        override fun bindData(t: Item, position: Int, viewType: Int) {

            GlideApp.with(itemView.context).placeholder(R.mipmap.ic_launcher)
                .load("https://thedientu.com${t.image?.trim()}")
                .into( itemView.imvLogo)

            if (t.isSeleted) itemView.ll_bg.background = itemView.resources.getDrawable(R.drawable.bg_border_selected)
            else itemView.ll_bg.background = itemView.resources.getDrawable(R.drawable.bg_border_black)
        }
    }


}