package com.eliving.tv.service.live.api

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Path

interface LiveApi {

    //Lấy catchup theo channel_id ( tra ve du lieu 7 ngay truoc , co url play)
    @GET("api/v1/catchup/{channel}/{date}")
    suspend fun getCatchup(
        @Path("channel") channel: String,
        @Path("date") id: String
    ): JsonObject

    //Lay EPG THEO CHANNEL_ID ( tra ve du lieu 7 ngay sau do )
    @GET("api/v1/catchup/channel_id/{id}")
    suspend fun getLive(
        @Path("id") id: String
    ): JsonObject

    //GET THONG TIN EPG HIEN TAI VA KE TIEP
    @GET("api/v1/channels/epglive/{id}")
    suspend fun getEpgInfor(
        @Path("id") id: String
    ): JsonObject

    //GET THONG TIN EPG HIEN TAI VA KE TIEP
    @GET("api/v1/channels/genre")
    suspend fun getLiveCategory(): JsonObject

    @GET("api/v1/channels")
    suspend fun getLiveChannel(): JsonObject


    @GET("api/v1/getDateEpg")
    suspend fun getDateEpg(): JsonObject

    @GET("api/v1/getDateCatchup")
    suspend fun getDateCatchup(): JsonObject

    //Lấy catchup theo channel_id ( tra ve du lieu 7 ngay truoc , co url play)
    @GET("api/v1/epgs/{channel}/{date}")
    suspend fun getEpg(
        @Path("channel") channel: String,
        @Path("date") id: String
    ): JsonObject

}