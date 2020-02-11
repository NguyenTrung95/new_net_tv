package com.nencer.nencerwallet.ui.main.home.adapter

import android.view.View
import com.nencer.nencerwallet.R
import com.nencer.nencerwallet.base.adapter.BaseAdapter
import com.nencer.nencerwallet.base.adapter.BaseViewHolder
import com.nencer.nencerwallet.ui.main.home.model.CardInfo
import kotlinx.android.synthetic.main.item_oder.view.*

class OrderDetailAdapter(private val  codeCopy:(value: String)->Unit
                         ,private val  serialCopy:(value: String)->Unit): BaseAdapter<CardInfo>() {
    override fun getLayoutId(viewType: Int): Int = R.layout.item_oder

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<CardInfo> {
        return HomeViewHolder(view,listItems)
    }

    inner class HomeViewHolder(itemView: View, listItem: MutableList<CardInfo>): BaseViewHolder<CardInfo>(itemView,listItem) {
        override fun bindData(t: CardInfo, position: Int, viewType: Int) {
            itemView.tv_code.text =t.code
            itemView.tv_seri.text = t.serial
            itemView.tv_count_name_card.text = t.name

            itemView.btn_copy_code.setOnClickListener { codeCopy.invoke(t.code.toString()) }
            itemView.btn_copy_seri.setOnClickListener { serialCopy.invoke(t.serial.toString()) }

        }
    }


}