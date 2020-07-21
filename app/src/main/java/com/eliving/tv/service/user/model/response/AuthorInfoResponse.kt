package com.eliving.tv.service.user.model.response

import android.os.Parcel
import android.os.Parcelable

open class AuthorInfoResponse() : Parcelable {
    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

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


