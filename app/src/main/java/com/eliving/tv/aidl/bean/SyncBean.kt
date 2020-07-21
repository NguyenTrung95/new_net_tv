package com.eliving.tv.aidl.bean

import java.io.Serializable

class SyncBean : Serializable {
    /* renamed from: id */
    var id = 0
    var sync = 0

    constructor() {}
    constructor(id: Int, sync2: Int) {
        this.id = id
        sync = sync2
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o !is SyncBean) {
            return false
        }
        val other = o
        return if (id == other.id && sync == other.sync) {
            true
        } else false
    }

    companion object {
        private const val serialVersionUID: Long = 1
    }
}