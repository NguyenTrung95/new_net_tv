package com.eliving.tv.aidl.bean

import java.io.Serializable

class UrlBean : Serializable {
    var description: String? = null
    var image: String? = null
    var isIsfinal = false
        private set
    var provider: String? = null
    var quality = 3
    var serial = 0
    var thumbnail: String? = null
    var title: String? = null
    var url: String? = null

    fun setIsfinal(isfinal2: Boolean) {
        isIsfinal = isfinal2
    }

    companion object {
        const val QUALITY_720P = 5
        const val QUALITY_HIGH = 3
        const val QUALITY_LOW = 1
        const val QUALITY_STANDARD = 2
        const val QUALITY_SUPER = 4
        const val QUALITY_UNKNOWN = 0
        private const val serialVersionUID: Long = 1
    }
}