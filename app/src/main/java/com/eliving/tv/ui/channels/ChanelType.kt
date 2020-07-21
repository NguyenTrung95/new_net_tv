package com.eliving.tv.ui.channels

import com.eliving.tv.R

enum class ChanelType {
    VOD, LIVE, EPG;

    val icon : Int
        get() = when(this) {
            VOD -> R.drawable.vod_title_icon
            EPG -> R.drawable.epg_title_icon
            else -> R.drawable.vod_title_icon

        }


    val typeName : String
        get() = when(this) {
            VOD -> "VOD"
            //LIVE -> "LIVE"
            EPG -> "EPG"
            else -> "EPG"
        }

    companion object {
        fun getType(typeName : String) : ChanelType {
            return when(typeName) {
                "VOD" -> ChanelType.VOD
                //"LIVE" -> ChanelType.LIVE
                else -> ChanelType.EPG
            }
        }

    }
}