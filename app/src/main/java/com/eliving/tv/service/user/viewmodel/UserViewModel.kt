package com.eliving.tv.service.user.viewmodel

import androidx.lifecycle.MutableLiveData
import com.eliving.tv.base.viewmodel.BaseViewModel
import com.eliving.tv.helper.DeviceUtil
import com.eliving.tv.service.user.repository.UserRepository
import com.google.gson.JsonObject


class UserViewModel(private val mUserRepository: UserRepository) : BaseViewModel() {
    /*login*/
    fun login(userName: String,password: String): MutableLiveData<JsonObject> {
        val mutableLiveData = MutableLiveData<JsonObject>()

        launch {
            mutableLiveData.value = mUserRepository.login(userName,password,DeviceUtil.macID(),DeviceUtil.deviceID())
            /*when(response.optString("status")){
                "success" -> mutableLiveData.value = LoginResponse(response)
                else -> mutableLiveData.value ="No response"
            }
*/
        }
        return  mutableLiveData
    }

    /*logout*/
    fun authorInfo(): MutableLiveData<JsonObject> {
        val mutableLiveData = MutableLiveData<JsonObject>()

        launch {
            mutableLiveData.value = mUserRepository.authorInfo()
        }
        return  mutableLiveData
    }

    /*register*/
    fun register(userName: String,password: String,email: String):MutableLiveData<JsonObject>{
        val mutableLiveData = MutableLiveData<JsonObject>()

        launch {
            mutableLiveData.value = mUserRepository.register(userName,password,email)

        }
        return  mutableLiveData
    }

    /*logout*/
    fun update(appId:String,appVersion:String): MutableLiveData<JsonObject> {
        val mutableLiveData = MutableLiveData<JsonObject>()

        launch {
            mutableLiveData.value = mUserRepository.update(appId,appVersion)
        }
        return  mutableLiveData
    }

    /*subcriptions*/
    fun subcriptions():MutableLiveData<Any>{
        val mutableLiveData = MutableLiveData<Any>()

        launch {
            mutableLiveData.value = mUserRepository.subcriptions()

        }
        return  mutableLiveData
    }


}
