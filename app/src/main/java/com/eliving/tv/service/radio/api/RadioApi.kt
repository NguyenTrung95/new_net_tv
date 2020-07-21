package com.eliving.tv.service.radio.api

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Path

interface RadioApi {

    @GET("api/v1/radios")
    suspend fun getRadios(): JsonObject

}