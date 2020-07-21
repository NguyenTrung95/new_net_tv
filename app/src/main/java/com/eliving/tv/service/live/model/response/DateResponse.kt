package com.eliving.tv.service.live.model.response

import android.os.Parcel
import android.os.Parcelable
import com.eliving.tv.ext.optString
import com.google.gson.JsonArray
import com.google.gson.JsonObject

open class DateResponse()  : Parcelable{
    val datas: ArrayList<DateEntity> = arrayListOf()

    constructor(parcel: Parcel) : this() {

    }

    constructor(jsonArr: JsonArray): this(){
        jsonArr.map{ jsonElement -> jsonElement.asJsonObject }.map{
            datas.add(DateEntity(it))
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
class DateEntity() :  Parcelable {
    var day: String? = ""
    var dayJ: String? = ""
    var isSelected : Boolean = false


    constructor(json : JsonObject):this(){
        day = json.optString("day")
        dayJ = json.optString("day_j")

    }

    constructor(parcel: Parcel) : this() {
        day = parcel.readString()
        dayJ = parcel.readString()

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(day)
        parcel.writeString(dayJ)


    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DateEntity> {
        override fun createFromParcel(parcel: Parcel): DateEntity {
            return DateEntity(parcel)
        }

        override fun newArray(size: Int): Array<DateEntity?> {
            return arrayOfNulls(size)
        }
    }


}

