package com.eliving.tv.service.live.model.response

import android.os.Parcel
import android.os.Parcelable
import com.eliving.tv.ext.optString
import com.google.gson.JsonArray
import com.google.gson.JsonObject

open class RadioResponse()  : Parcelable{
    val datas: ArrayList<RadioEntity> = arrayListOf()

    constructor(parcel: Parcel) : this() {

    }

    constructor(jsonArr: JsonArray): this(){
        jsonArr.map{ jsonElement -> jsonElement.asJsonObject }.map{
            datas.add(RadioEntity(it))
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RadioEntity> {
        override fun createFromParcel(parcel: Parcel): RadioEntity {
            return RadioEntity(parcel)
        }

        override fun newArray(size: Int): Array<RadioEntity?> {
            return arrayOfNulls(size)
        }
    }
}
class RadioEntity() :  Parcelable {
    var radio_name: String? = ""
    var radio_url: String? = ""


    constructor(json : JsonObject):this(){
        radio_name = json.optString("radio_name")
        radio_url = json.optString("radio_url")

    }

    constructor(parcel: Parcel) : this() {
        radio_name = parcel.readString()
        radio_url = parcel.readString()


    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(radio_name)
        parcel.writeString(radio_url)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RadioEntity> {
        override fun createFromParcel(parcel: Parcel): RadioEntity {
            return RadioEntity(parcel)
        }

        override fun newArray(size: Int): Array<RadioEntity?> {
            return arrayOfNulls(size)
        }
    }


}

