package com.eliving.tv.aidl.bean

import java.io.Serializable
import java.util.*

class MediaListBean : Serializable {
    var list: ArrayList<MediaBean>? = null
    var pagecount = 0
    var pageindex = 0
    var pagesize = 0
    var totalcount = 0

    companion object {
        private const val serialVersionUID: Long = 1
    }
}