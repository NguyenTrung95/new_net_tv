
package com.eliving.tv.ui.channels

import android.view.View
import com.eliving.tv.R
import com.eliving.tv.base.adapter.BaseAdapter
import com.eliving.tv.base.adapter.BaseViewHolder
import com.eliving.tv.base.adapter.OnItemClickListener
import com.eliving.tv.service.live.model.response.ChannelsEntity
import com.eliving.tv.service.live.model.response.DateEntity
import com.eliving.tv.service.user.model.response.LiveResponse
import kotlinx.android.synthetic.main.item_date.view.*
import kotlinx.android.synthetic.main.item_selector.view.*
import kotlinx.android.synthetic.main.livelist.view.*

class AdapterLive(listener: OnItemClickListener<LiveResponse>): BaseAdapter<LiveResponse>(listener) {
    override fun getLayoutId(viewType: Int): Int = R.layout.livelist

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<LiveResponse> {
        return ChannelsViewHolder(view,listItems, mOnItemClickListener!!)
    }

    inner class ChannelsViewHolder(itemView: View, listItem: MutableList<LiveResponse>, listener: OnItemClickListener<LiveResponse>): BaseViewHolder<LiveResponse>(itemView,listItem,listener) {
        override fun bindData(t: LiveResponse, position: Int, viewType: Int) {
            itemView.tv_live_channelnum.text = t.channel_number
            itemView.newname.text = t.title
            if (t.isSelected) {
                itemView.setBackgroundResource(R.color.orange_1)
            } else {
                itemView.setBackgroundResource(R.drawable.live_cs_list_sel)
            }
        }
    }
}





