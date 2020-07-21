
package com.eliving.tv.ui.channels

import android.view.View
import com.eliving.tv.R
import com.eliving.tv.base.adapter.BaseAdapter
import com.eliving.tv.base.adapter.BaseViewHolder
import com.eliving.tv.base.adapter.OnItemClickListener
import com.eliving.tv.service.live.model.response.ChannelsEntity
import kotlinx.android.synthetic.main.item_selector.view.*

class VodAdapter(listener: OnItemClickListener<ChannelsEntity>): BaseAdapter<ChannelsEntity>(listener) {
    override fun getLayoutId(viewType: Int): Int = R.layout.item_selector

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<ChannelsEntity> {
        return ChannelsViewHolder(view,listItems, mOnItemClickListener!!)
    }

    inner class ChannelsViewHolder(itemView: View, listItem: MutableList<ChannelsEntity>, listener: OnItemClickListener<ChannelsEntity>): BaseViewHolder<ChannelsEntity>(itemView,listItem,listener) {
        override fun bindData(t: ChannelsEntity, position: Int, viewType: Int) {
            itemView.tv_time.text = t.program_start?.split(" ")?.get(1) ?: ""
            itemView.tv_channels_name.text = t.short_name

            if (itemView.hasFocus()){
                itemView.tv_time.setTextColor(itemView.resources.getColor(R.color.black))
                itemView.tv_channels_name.setTextColor(itemView.resources.getColor(R.color.black))
            }else{
                itemView.tv_time.setTextColor(itemView.resources.getColor(R.color.white))
                itemView.tv_channels_name.setTextColor(itemView.resources.getColor(R.color.white))
            }

        }
    }
}





