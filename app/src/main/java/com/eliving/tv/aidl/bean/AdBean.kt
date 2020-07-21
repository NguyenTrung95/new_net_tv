package com.eliving.tv.aidl.bean

import java.io.Serializable

class AdBean : Serializable {
    var content: String? = null
    var duration = 0
    var extend: String? = null

    /* renamed from: id */
    var id = 0
    var meta = 0
    var title: String? = null
    var type = 0

    companion object {
        const val META_IMAGE = 2
        const val META_TEXT = 3
        const val META_VIDEO = 1
        const val TYPE_ONFIELD = 4
        const val TYPE_ONPAUSE = 2
        const val TYPE_ONPLAY = 1
        const val TYPE_ONPLAYPAUSE = 3
        private const val serialVersionUID: Long = 1
    }
}