package com.eliving.tv.service.user.model.response

import android.os.Parcel
import android.os.Parcelable
import com.eliving.tv.ext.optInt
import com.eliving.tv.ext.optJsonObject
import com.eliving.tv.ext.optString
import com.google.gson.JsonObject

open class LiveResponse() : Parcelable {

    var id: Int? = 0
    var title: String?=""
    var channel_number: String?=""
    var stream_url: String?=""
    var token_url: String?=""
    var isSelected : Boolean = false

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        title = parcel.readString()
        channel_number = parcel.readString()
        stream_url = parcel.readString()
        token_url = parcel.readString()
    }

    constructor(json : JsonObject):this(){
        id = json?.optInt("id")
        title = json?.optString("title")
        channel_number = json?.optString("channel_number")
        stream_url = json?.optString("channel_streams.stream_url")
        token_url = json?.optString("channel_streams.token_url")
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(title)
        parcel.writeString(channel_number)
        parcel.writeString(stream_url)
        parcel.writeString(token_url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LiveResponse> {
        override fun createFromParcel(parcel: Parcel): LiveResponse {
            return LiveResponse(parcel)
        }

        override fun newArray(size: Int): Array<LiveResponse?> {
            return arrayOfNulls(size)
        }
    }


}


