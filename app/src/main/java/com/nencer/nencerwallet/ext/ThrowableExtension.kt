package com.nencer.nencerwallet.ext
import org.json.JSONObject
import retrofit2.HttpException
import java.net.UnknownHostException

/**
 * Read http message and update to message
 */
val Throwable.httpNormalize: Throwable
    get() {
        return (this as? HttpException)?.takeIf { it.response()?.errorBody() != null }?.run {
            val json = JSONObject(response()?.errorBody()!!.string())
            val message = json.optJSONObject("error")?.optString("message") ?: json.toString()
            Throwable(message, this)
        } ?: this
    }
