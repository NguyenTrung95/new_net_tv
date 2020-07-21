package com.eliving.tv.aidl.bean

import java.io.Serializable

class LogBean : Serializable {
    var cpu = 0
    var description: String? = null
    var disk_free = 0
    var disk_total = 0
    var disk_used = 0
    var down_flow = 0
    var downkb = 0
    var duration_percent = 0
    var duration_total = 0
    var duration_watch = 0
    var end_utc: Long = 0
    var extend = "unKnow"
    var extra: String? = null

    /* renamed from: io */
    var io = 0
    var mac: String? = null
    var media_id: String? = null
    var mem = 0
    var mem_free = 0
    var mem_total = 0
    var mem_used = 0
    var name: String? = null
    var new_version: String? = null
    var ois: String? = null
    var old_version: String? = null
    var package_name: String? = null
    var path: String? = null
    var rwflag: String? = null
    var serial: Long = 0
    var start_utc: Long = 0
    var subtype = 999
    var terminal_id: String? = null
    var type = 1
    var up_flow = 0
    var upgrade_type: String? = null
    var upkb = 0
    var url: String? = null
    var user_id: String? = null

    enum class action_extend {
        login, logout, upgrade, standby, reboot, play, pause, resume, stop, install, favorite
    }

    enum class error_extend
    enum class exception_extend {
        play, file, upgrade, install
    }

    enum class execution_extend
    enum class monitor_extend {
        system
    }

    enum class statistics_extend {
        channel, vod, ad, flow
    }

    enum class warning_extend

    companion object {
        const val ACTION_LOG = 4
        const val ERROR_LOG = 3
        const val EXCEPTION_LOG = 2
        const val EXECUTION_LOG = 1
        const val MONITOR_LOG = 8
        const val SERVER = 2
        const val STATISTICS_LOG = 7
        const val TERMINAL = 1
        const val WARNING_LOG = 9
        private const val serialVersionUID: Long = 1
    }
}