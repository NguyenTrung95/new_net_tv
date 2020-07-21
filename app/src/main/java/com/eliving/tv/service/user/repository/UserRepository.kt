package com.eliving.tv.service.user.repository

import com.eliving.tv.helper.DeviceUtil
import com.eliving.tv.service.user.api.UserApi

class UserRepository(private val api: UserApi) {

    /*login*/
    suspend fun login(userName: String, password: String,device_mac_address:String,deviceId:String) =
        api.login(
            userName,
            password,
            device_mac_address,
            deviceId,
            "1",
            "1",
            "newapp0",
            DeviceUtil.getVersionCode()

        )

    /*author info*/
    suspend fun authorInfo() =
        api.getAuthorInfo(device_id = DeviceUtil.deviceID() ,device_mac_address = DeviceUtil.macID())

    /*register*/
    suspend fun register(userName: String, password: String,email: String) =
        api.register(userName, password, email)

    /*update*/
    suspend fun update(appid: String, appVersion: String) =
        api.update(appid, appVersion)

    /*subcriptions*/
    suspend fun subcriptions() =
        api.subcriptions()


}
