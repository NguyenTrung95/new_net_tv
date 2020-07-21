package com.eliving.tv.aidl.bean

import java.io.Serializable

class EPGBean : Serializable {
    var channelId: String? = null
    var description: String? = null
    var endUtc: Long = 0
    var title: String? = null
    var type: String? = null
    var urls: List<EPGUrlBean>? = null
    var utc: Long = 0

    override fun toString(): String {
        return "programName = " + title + " utc = " + utc
    }

    companion object {
        private const val serialVersionUID: Long = 1
    }
}