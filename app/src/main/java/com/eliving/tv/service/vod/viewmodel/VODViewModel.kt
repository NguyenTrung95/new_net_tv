package com.eliving.tv.service.vod.viewmodel

import androidx.lifecycle.MutableLiveData
import com.eliving.tv.base.viewmodel.BaseViewModel
import com.eliving.tv.ext.optString
import com.eliving.tv.service.user.model.response.LoginResponse
import com.eliving.tv.service.vod.repository.VODRepository
import com.google.gson.JsonObject

class VODViewModel(private val repository: VODRepository) : BaseViewModel() {
    /*vod category*/
    fun category(): MutableLiveData<Any> {
        val mutableLiveData = MutableLiveData<Any>()

        launch {
            val response = repository.getVODCategory()
            when(response.optString("status")){
                "success" -> mutableLiveData.value = LoginResponse(response)

            }

        }
        return  mutableLiveData
    }

    /*vod*/
    fun vod(categoryId: String,page: Int):MutableLiveData<JsonObject>{
        val mutableLiveData = MutableLiveData<JsonObject>()

        launch {
            mutableLiveData.value = repository.getVOD(categoryId,page)
        }
        return  mutableLiveData
    }


}
