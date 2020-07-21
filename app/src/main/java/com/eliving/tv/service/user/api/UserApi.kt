package com.eliving.tv.service.user.api

import com.google.gson.JsonObject
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {

    @POST("auth")
    @FormUrlEncoded
    suspend fun getAuthorInfo(
        @Field("device_id") device_id: String,
        @Field("device_mac_address") device_mac_address: String
    ): JsonObject



    @POST("reg")
    @FormUrlEncoded
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("email") email: String
        ): JsonObject


    @POST("login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("device_mac_address") device_mac_address: String,
        @Field("device_id") device_id: String,
        @Field("ntype") ntype: String,
        @Field("appid") appid: String,
        @Field("app_name") app_name: String,
        @Field("app_version") app_version: String): JsonObject

    @POST("update")
    @FormUrlEncoded
    suspend fun update(
        @Field("appid") appid: String,
        @Field("app_version") app_version: String
    ): JsonObject

    @POST("api/v1/subcriptions")
    @FormUrlEncoded
    suspend fun subcriptions(): JsonObject


}