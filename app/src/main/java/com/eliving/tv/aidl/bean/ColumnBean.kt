package com.eliving.tv.aidl.bean

import java.io.Serializable

class ColumnBean : Serializable {
    /* renamed from: id */
    var id = 0
    var isLeaf = false
    var pid = 0
    var title: String? = null
    var type: String? = null

    constructor() {}
    constructor(
        id: Int,
        pid2: Int,
        title2: String?,
        type2: String?,
        leaf2: Boolean
    ) {
        this.id = id
        pid = pid2
        title = title2
        type = type2
        isLeaf = leaf2
    }

    override fun equals(bean: Any?): Boolean {
        if (bean == null) {
            return false
        }
        if (this === bean) {
            return true
        }
        if (bean !is ColumnBean) {
            return false
        }
        val other = bean
        if (id != other.id || pid != other.pid) {
            return false
        }
        if (title == null) {
            if (other.title != null) {
                return false
            }
        } else if (title != other.title) {
            return false
        }
        if (type == null) {
            if (other.type != null) {
                return false
            }
        } else if (type != other.type) {
            return false
        }
        return true
    }

    override fun toString(): String {
        return "id = " + id + " pid = " + pid + " title = " + title + " type  = " + type
    }

    companion object {
        private const val serialVersionUID: Long = 1
    }
}