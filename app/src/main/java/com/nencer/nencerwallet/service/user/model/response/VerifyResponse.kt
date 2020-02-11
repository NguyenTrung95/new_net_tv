package com.nencer.nencerwallet.service.user.model.response

import com.google.gson.JsonObject
import com.nencer.nencerwallet.ext.optInt
import com.nencer.nencerwallet.ext.optJsonObject
import com.nencer.nencerwallet.ext.optString

class VerifyResponse() {
    var phone: String?=""
    var remember: Int?=0
    var token: String?=""
    var deviceId: String?=""
    var session_id:String?=""

    constructor(jsonObject: JsonObject):this(){
        val data = jsonObject.optJsonObject("info")
        phone = data?.optString("phone")
        remember = data?.optInt("remember")
        token = data?.optString("token")
        deviceId = data?.optString("device_id")
        session_id = data?.optString("session_id")
    }


}