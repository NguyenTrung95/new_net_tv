package com.eliving.tv.aidl.bean

import java.io.Serializable

class CategoryBean : Serializable {
    /* renamed from: id */
    var id = 0
    var title: String? = null
    var total = 0

    companion object {
        private const val serialVersionUID: Long = 1
    }
}