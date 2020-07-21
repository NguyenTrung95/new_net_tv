package com.eliving.tv.service.live.viewmodel

import androidx.lifecycle.MutableLiveData
import com.eliving.tv.base.viewmodel.BaseViewModel
import com.eliving.tv.ext.optInt
import com.eliving.tv.ext.optJsonArray
import com.eliving.tv.service.live.model.response.DateEntity
import com.eliving.tv.service.live.model.response.DateResponse
import com.eliving.tv.service.live.repository.LiveRepository
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_chanels.*
import java.util.*


class LiveViewModel(private val repository: LiveRepository) : BaseViewModel() {
    /*catchup*/
    fun getCatchup(channel: String,date: String): MutableLiveData<JsonObject> {
        val mutableLiveData = MutableLiveData<JsonObject>()

        launch {
            mutableLiveData.value = repository.getCatchup(channel,date)

        }
        return  mutableLiveData
    }

    /*live*/
    fun getLive(id: String): MutableLiveData<JsonObject> {
        val mutableLiveData = MutableLiveData<JsonObject>()

        launch {
            mutableLiveData.value = repository.getLive(id)

        }
        return  mutableLiveData
    }

    /*epg infor*/
    fun getEpgInfor(id: String): MutableLiveData<JsonObject> {
        val mutableLiveData = MutableLiveData<JsonObject>()

        launch {
            mutableLiveData.value = repository.getEpgInfor(id)
            /*when(response.optString("status")){
                "success" -> mutableLiveData.value = LoginResponse(response)
                else -> mutableLiveData.value ="No response"
            }
*/
        }
        return  mutableLiveData
    }

    /*catchup*/
    fun getLiveCategory(): MutableLiveData<JsonObject> {
        val mutableLiveData = MutableLiveData<JsonObject>()

        launch {
            mutableLiveData.value = repository.getLiveCategory()
            /*when(response.optString("status")){
                "success" -> mutableLiveData.value = LoginResponse(response)
                else -> mutableLiveData.value ="No response"
            }
*/
        }
        return  mutableLiveData
    }

    fun getLiveChannel(): MutableLiveData<JsonObject> {
        val mutableLiveData = MutableLiveData<JsonObject>()

        launch {

            mutableLiveData.value = repository.getLiveChannel()
        }
        return  mutableLiveData
    }

    fun getEpgDate(): MutableLiveData<DateResponse> {
        val mutableLiveData = MutableLiveData<DateResponse>()

        launch {

            val  json = repository.getEpgDate()
            val  code = json.optInt("code")
            if (code == 200){
                mutableLiveData.value = json.optJsonArray("data")?.let { DateResponse(it) }
            }
        }
        return  mutableLiveData
    }

    fun getCatchupDate(): MutableLiveData<DateResponse> {
        val mutableLiveData = MutableLiveData<DateResponse>()

        launch {

            val  json = repository.getCatchupDate()
            val  code = json.optInt("code")
            if (code == 200){
                mutableLiveData.value = json.optJsonArray("data")?.let { DateResponse(it) }
            }        }
        return  mutableLiveData
    }


    fun getEpg(channel: String,date: String): MutableLiveData<JsonObject> {
        val mutableLiveData = MutableLiveData<JsonObject>()

        launch {
            mutableLiveData.value = repository.getEpg(channel,date)

        }
        return  mutableLiveData
    }



}
