package com.eliving.tv.service.radio.viewmodel

import androidx.lifecycle.MutableLiveData
import com.eliving.tv.base.viewmodel.BaseViewModel
import com.eliving.tv.service.radio.repository.RadioRepository
import com.google.gson.JsonObject

class RadioViewModel(private val repository: RadioRepository) : BaseViewModel() {
    /*vod*/
    fun radios():MutableLiveData<JsonObject>{
        val mutableLiveData = MutableLiveData<JsonObject>()

        launch {
            mutableLiveData.value = repository.getRadios()
        }
        return  mutableLiveData
    }


}
