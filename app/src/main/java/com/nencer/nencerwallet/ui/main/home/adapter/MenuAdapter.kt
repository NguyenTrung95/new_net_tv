package com.nencer.nencerwallet.ui.main.home.adapter

import android.view.View
import com.nencer.nencerwallet.R
import com.nencer.nencerwallet.base.adapter.BaseAdapter
import com.nencer.nencerwallet.base.adapter.BaseViewHolder
import com.nencer.nencerwallet.base.adapter.OnItemClickListener
import kotlinx.android.synthetic.main.item_menu.view.*

class MenuAdapter(listener: OnItemClickListener<Pair<Int,String>>): BaseAdapter<Pair<Int,String>>(listener) {
    override fun getLayoutId(viewType: Int): Int = R.layout.item_menu

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<Pair<Int,String>> {
        return HomeViewHolder(view,listItems, mOnItemClickListener!!)
    }

    inner class HomeViewHolder(itemView: View, listItem: MutableList<Pair<Int,String>>, listener: OnItemClickListener<Pair<Int,String>>): BaseViewHolder<Pair<Int,String>>(itemView,listItem,listener) {
        override fun bindData(t: Pair<Int,String>, position: Int, viewType: Int) {

            itemView.imvMenu.setImageResource(t.first)
            itemView.tvTitle.text = t.second
        }
    }


}