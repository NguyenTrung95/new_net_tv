package com.eliving.tv.aidl.bean

import java.io.Serializable

class RealUrlBean : Serializable {
    var quality = 0
    var qualitys: String? = null
    var title: String? = null
    var url: String? = null

    companion object {
        private const val serialVersionUID: Long = 1
    }
}