
package com.eliving.tv.ui.radio

import android.view.View
import com.eliving.tv.R
import com.eliving.tv.base.adapter.BaseAdapter
import com.eliving.tv.base.adapter.BaseViewHolder
import com.eliving.tv.base.adapter.OnItemClickListener
import com.eliving.tv.service.live.model.response.RadioEntity
import kotlinx.android.synthetic.main.item_radio.view.*

class RadioAdapter(listener: OnItemClickListener<RadioEntity>): BaseAdapter<RadioEntity>(listener) {
    override fun getLayoutId(viewType: Int): Int = R.layout.item_radio

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<RadioEntity> {
        return ChannelsViewHolder(view,listItems, mOnItemClickListener!!)
    }

    inner class ChannelsViewHolder(itemView: View, listItem: MutableList<RadioEntity>, listener: OnItemClickListener<RadioEntity>): BaseViewHolder<RadioEntity>(itemView,listItem,listener) {
        override fun bindData(t: RadioEntity, position: Int, viewType: Int) {
            itemView.tv_radio_name.text = t.radio_name

        }
    }
}





