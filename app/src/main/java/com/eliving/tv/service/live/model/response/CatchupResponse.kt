package com.eliving.tv.service.live.model.response

import android.os.Parcel
import android.os.Parcelable
import com.eliving.tv.ext.optString
import com.google.gson.JsonArray
import com.google.gson.JsonObject

open class CatchupResponse()  : Parcelable{
    val datas: ArrayList<ChannelsEntity> = arrayListOf()

    constructor(parcel: Parcel) : this() {

    }

    constructor(jsonArr: JsonArray): this(){
        jsonArr.map{ jsonElement -> jsonElement.asJsonObject }.map{
            datas.add(ChannelsEntity(it))
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CatchupResponse> {
        override fun createFromParcel(parcel: Parcel): CatchupResponse {
            return CatchupResponse(parcel)
        }

        override fun newArray(size: Int): Array<CatchupResponse?> {
            return arrayOfNulls(size)
        }
    }
}
class ChannelsEntity() :  Parcelable {
    var delay: String? = ""
    var id: String? = ""
    var short_description: String? = ""
    var short_name: String? = ""
    var duration_seconds: String? = ""
    var program_start: String? = ""
    var program_end: String? = ""
    var long_description: String? = ""
    var channeld: String? = ""
    var channelTitle: String? = ""
    var channel_number: String? = ""
    var schedulesId: String? = ""
    var stream_url: String? = ""
    var isSelected = false

    constructor(json : JsonObject):this(){
        delay = json.optString("delay")
        id = json.optString("id")
        short_description = json.optString("short_description")
        short_name = json.optString("short_name")
        duration_seconds = json.optString("duration_seconds")
        program_start = json.optString("program_start")
        program_end = json.optString("program_end")
        long_description = json.optString("long_description")
        channeld = json.optString("channel.id")
        channelTitle = json.optString("channel.title")
        channel_number = json.optString("channel.channel_number")
        schedulesId = json.optString("program_schedules.id")
        stream_url = json.optString("channel.stream_url")

    }

    constructor(parcel: Parcel) : this() {
        delay = parcel.readString()
        id = parcel.readString()
        short_description = parcel.readString()
        short_name = parcel.readString()
        duration_seconds = parcel.readString()
        program_start = parcel.readString()
        program_end = parcel.readString()
        long_description = parcel.readString()
        channeld = parcel.readString()
        channelTitle = parcel.readString()
        channel_number = parcel.readString()
        schedulesId = parcel.readString()
        stream_url = parcel.readString()

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(delay)
        parcel.writeString(id)
        parcel.writeString(short_description)
        parcel.writeString(short_name)
        parcel.writeString(duration_seconds)
        parcel.writeString(program_start)
        parcel.writeString(program_end)
        parcel.writeString(long_description)
        parcel.writeString(channeld)
        parcel.writeString(channelTitle)
        parcel.writeString(channel_number)
        parcel.writeString(schedulesId)
        parcel.writeString(stream_url)

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

