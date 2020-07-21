package com.eliving.tv.aidl.bean

import org.xmlpull.v1.XmlPullParser
import java.io.Serializable

class TagBean : Serializable {
    /* renamed from: id */
    var id = 0
    var title: String = XmlPullParser.NO_NAMESPACE

    companion object {
        private const val serialVersionUID: Long = 1
    }
}