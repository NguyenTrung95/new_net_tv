package com.eliving.tv.ui.launcher.nettv.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.eliving.tv.R
import com.eliving.tv.service.user.model.response.LiveResponse
import java.util.*

class LiveAdapter(
    private val context: Context,
    private val listData: ArrayList<LiveResponse>
) : BaseAdapter() {
    private val layoutInflater: LayoutInflater
    override fun getCount(): Int {
        return listData.size
    }

    override fun getItem(position: Int): LiveResponse {
        return listData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.livelist, null)
            holder = ViewHolder()
            holder.channelNum =
                convertView.findViewById<View>(R.id.tv_live_channelnum) as TextView
            holder.newName = convertView.findViewById<View>(R.id.newname) as TextView
            convertView.tag = holder
        } else {
            holder =
                convertView.tag as ViewHolder
        }
        val liveObject = listData[position]
        holder.channelNum!!.text = liveObject.channel_number
        holder.newName!!.text = liveObject.title
        if (liveObject.isSelected) {
            convertView?.setBackgroundResource(R.color.yellow_2)

        } else {
            convertView?.setBackgroundResource(R.drawable.live_cs_list_sel)

        }
        return convertView!!
    }

    internal class ViewHolder {
        var channelNum: TextView? = null
        var newName: TextView? = null
    }

    init {
        layoutInflater = LayoutInflater.from(context)
    }
}