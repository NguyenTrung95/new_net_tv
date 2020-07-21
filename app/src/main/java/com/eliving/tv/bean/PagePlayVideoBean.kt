package com.eliving.tv.bean

class PagePlayVideoBean {
    var channelIconUrl: String? = null
    var channelName: String? = null
    var channelNo: String? = null
    var curPlayType: String? = null
    private var mPlayUrl: String? = null
    private var mSeekFileName: String? = null
    private var mSeekName: String? = null

    fun getmSeekFileName(): String? {
        return mSeekFileName
    }

    fun setmSeekFileName(mSeekFileName2: String?) {
        mSeekFileName = mSeekFileName2
    }

    fun getmSeekName(): String? {
        return mSeekName
    }

    fun setmSeekName(mSeekName2: String?) {
        mSeekName = mSeekName2
    }

    fun getmPlayUrl(): String? {
        return mPlayUrl
    }

    fun setmPlayUrl(mPlayUrl2: String?) {
        mPlayUrl = mPlayUrl2
    }
}
