package com.eliving.tv.service.live.model.response

import android.os.Parcel
import android.os.Parcelable
import com.eliving.tv.ext.optString
import com.google.gson.JsonArray
import com.google.gson.JsonObject

open class ChannelsResponse()  : Parcelable{
    val datas: ArrayList<Channels> = arrayListOf()

    constructor(parcel: Parcel) : this() {

    }

    constructor(jsonArr: JsonArray): this(){
        jsonArr.map{ jsonElement -> jsonElement.asJsonObject }.map{
            datas.add(Channels(it))
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChannelsResponse> {
        override fun createFromParcel(parcel: Parcel): ChannelsResponse {
            return ChannelsResponse(parcel)
        }

        override fun newArray(size: Int): Array<ChannelsResponse?> {
            return arrayOfNulls(size)
        }
    }
}
class Channels() :  Parcelable {
    var id: String? = ""
    var genre_id: String? = ""
    var channel_number: String? = ""
    var title: String? = ""
    var icon_url: String? = ""
    var pin_protected: String? = ""
    var stream_source_id: String? = ""
    var stream_url: String? = ""
    var stream_format: String? = ""
    var token: String? = ""
    var is_octoshape: String? = ""
    var drm_platform: String? = ""
    var encryption: String? = ""
    var encryption_url: String? = ""
    var channelsId: String? = ""

    constructor(json : JsonObject):this(){
        id = json.optString("id")
        genre_id = json.optString("genre_id")
        channel_number = json.optString("channel_number")
        title = json.optString("title")
        icon_url = json.optString("icon_url")
        pin_protected = json.optString("pin_protected")
        stream_source_id = json.optString("channel_streams.stream_source_id")
        stream_url = json.optString("channel_streams.stream_url")
        stream_format = json.optString("channel_streams.stream_format")
        token = json.optString("channel_streams.token")
        is_octoshape = json.optString("channel_streams.is_octoshape")
        drm_platform = json.optString("channel_streams.drm_platform")
        encryption = json.optString("channel_streams.encryption")
        encryption_url = json.optString("channel_streams.encryption_url")
        channelsId = json.optString("favorite_channels.id")

    }

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        genre_id = parcel.readString()
        channel_number = parcel.readString()
        title = parcel.readString()
        icon_url = parcel.readString()
        pin_protected = parcel.readString()
        stream_source_id = parcel.readString()
        stream_url = parcel.readString()
        stream_format = parcel.readString()
        token = parcel.readString()
        is_octoshape = parcel.readString()
        drm_platform = parcel.readString()
        encryption = parcel.readString()
        encryption_url = parcel.readString()
        channelsId = parcel.readString()

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(genre_id)
        parcel.writeString(channel_number)
        parcel.writeString(title)
        parcel.writeString(icon_url)
        parcel.writeString(pin_protected)
        parcel.writeString(stream_source_id)
        parcel.writeString(stream_url)
        parcel.writeString(stream_format)
        parcel.writeString(token)
        parcel.writeString(is_octoshape)
        parcel.writeString(drm_platform)
        parcel.writeString(encryption)
        parcel.writeString(encryption_url)
        parcel.writeString(channelsId)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChannelsEntity> {
        override fun createFromParcel(parcel: Parcel): ChannelsEntity {
            return ChannelsEntity(parcel)
        }

        override fun newArray(size: Int): Array<ChannelsEntity?> {
            return arrayOfNulls(size)
        }
    }


}

