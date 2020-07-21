package com.eliving.tv.aidl.bean

import org.xmlpull.v1.XmlPullParser
import java.io.Serializable

class AssetBean : Serializable {
    var balances = 0
    var op_amount = 0
    var op_type = 0
    var op_utc: Long = 0
    var service_id = XmlPullParser.NO_NAMESPACE
    var user_id = XmlPullParser.NO_NAMESPACE

    companion object {
        const val TYPE_RECHARGE = 1
        const val TYPE_SUBSCRIBE = 2
        const val TYPE_UNSUBSCRIBE = 3
        private const val serialVersionUID: Long = 1
    }
}