
package com.eliving.tv.ui.channels

import android.view.View
import com.eliving.tv.R
import com.eliving.tv.base.adapter.BaseAdapter
import com.eliving.tv.base.adapter.BaseViewHolder
import com.eliving.tv.base.adapter.OnItemClickListener
import com.eliving.tv.service.live.model.response.ChannelsEntity
import com.eliving.tv.service.live.model.response.DateEntity
import kotlinx.android.synthetic.main.item_date.view.*
import kotlinx.android.synthetic.main.item_selector.view.*

class DateTimeAdapter(listener: OnItemClickListener<DateEntity>): BaseAdapter<DateEntity>(listener) {
    override fun getLayoutId(viewType: Int): Int = R.layout.item_date

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<DateEntity> {
        return ChannelsViewHolder(view,listItems, mOnItemClickListener!!)
    }

    inner class ChannelsViewHolder(itemView: View, listItem: MutableList<DateEntity>, listener: OnItemClickListener<DateEntity>): BaseViewHolder<DateEntity>(itemView,listItem,listener) {
        override fun bindData(t: DateEntity, position: Int, viewType: Int) {
            itemView.btn_date.text = t.day
            if (t.isSelected) {
                itemView.lnViewDate?.setBackgroundResource(R.drawable.epg_date_checked)
            } else {
                itemView.lnViewDate?.setBackgroundResource(R.drawable.epg_date_unchecked)

            }
        }
    }
}





