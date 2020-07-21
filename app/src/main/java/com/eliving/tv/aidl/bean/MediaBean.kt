package com.eliving.tv.aidl.bean

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class MediaBean : Serializable {
    var actor: String? = null
    var ads: ArrayList<AdBean>? = null
    var area: String? = null
    var bitrate = 0
    var byteLen: Long = 0
    var category: String? = null
    var channelNumber = 0
    var columnId = 0
    var curSerial = 0
    var description: String? = null
    var dialogue: String? = null
    var director: String? = null

    /* renamed from: id */
    var id: String? = null
    var image: String? = null
    var limitLevel = 0
    var meta = 0
    var packageName: String? = null
    var pagecount = 0
    var playCount = 0
    var priceDay = 0
    var priceMonth = 0
    var priceYear = 0
    var provider: String? = null
    var recommendLevel = 0
    var releasetime: Long = 0
    var score = 0
    var screenwriter: String? = null
    var state = 0
    var supportPlayback = 0
    var tag: String? = null
    var thumbnail: String? = null
    var timeLen = 0
    var title: String? = null
    var totalSerial = 0
    var totalcount = 0
    var urls: ArrayList<UrlBean>? = null
    var versionCode: String? = null
    var versionName: String? = null
    var year = 0

    override fun toString(): String {
        return "MediaBean [id=" + id + ", meta=" + meta + ", title=" + title + ", image=" + image + ", thumbnail=" + thumbnail + ", category=" + this.category + ", area=" + area + ", provider=" + provider + ", tag=" + tag + ", score=" + score + ", playCount=" + playCount + ", actor=" + actor + ", director=" + director + ", screenwriter=" + screenwriter + ", releasetime=" + releasetime + ", year=" + year + ", description=" + description + ", dialogue=" + dialogue + ", urls=" + urls + ", ads=" + ads + ", totalSerial=" + totalSerial + ", curSerial=" + curSerial + ", byteLen=" + byteLen + ", timeLen=" + timeLen + ", bitrate=" + bitrate + ", recommendLevel=" + recommendLevel + ", limitLevel=" + limitLevel + ", channelNumber=" + channelNumber + ", supportPlayback=" + supportPlayback + ", versionName=" + versionName + ", versionCode=" + versionCode + ", packageName=" + packageName + ", state=" + state + ", priceDay=" + priceDay + ", priceMonth=" + priceMonth + ", priceYear=" + priceYear + ", columnId=" + columnId + ", pagecount=" + pagecount + ", totalcount=" + totalcount + "]"
    }

    companion object {
        const val META_APK = 30
        const val META_DLNA_IMAGE = -4
        const val META_DLNA_MUSIC = -6
        const val META_DLNA_VIDEO = -5
        const val META_HTML = 20
        const val META_IMAGE = 40
        const val META_LIVE_ABR = 5
        const val META_LIVE_CHANNEL = 0
        const val META_LOCAL_IMAGE = -1
        const val META_LOCAL_MUSIC = -3
        const val META_LOCAL_VIDEO = -2
        const val META_SERVICE_PACKAGE = 10
        const val META_TEXT = 50
        const val META_VOD_ABR = 6
        const val META_VOD_FILMS = 3
        const val META_VOD_GROUP = 4
        const val META_VOD_PROGRAMS = 2
        const val META_VOD_SINGLE = 1
        const val STATE_DOWNLOADED = 2
        const val STATE_EMPTY = 1
        const val STATE_INSTALLED = 3
        const val STATE_UNKNOWN = 0
        private const val serialVersionUID: Long = 1
    }
}