package com.eliving.tv.service.vod.api

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Path

interface VODApi {

    @GET("api/v1/vods/cat")
    suspend fun getVodCategory(): JsonObject

    @GET("api/v1/vods/cat/{category_id}/{page}")
    suspend fun getVOD(
        @Path("category_id") category_id: String,//category id
        @Path("page") page: Int// page
    ): JsonObject

}