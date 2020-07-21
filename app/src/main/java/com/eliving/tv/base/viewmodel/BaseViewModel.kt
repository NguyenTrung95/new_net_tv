package com.eliving.tv.base.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException


open class BaseViewModel : ViewModel() {

    val mStateLiveData = MutableLiveData<StateActionEvent>()
    val errorMsg = MutableLiveData<String>()
    fun launch(block: suspend CoroutineScope.() -> Unit) {

        viewModelScope.launch {
            try {
                mStateLiveData.value = LoadState
                block()
                mStateLiveData.value = SuccessState
            } catch (e: Exception) {
                mStateLiveData.value = ErrorState(e.message)
                if (e is HttpException){
                    val response = (e as HttpException).response()?.errorBody()?.source()?.readByteString()?.utf8()
                    val jsonObject =
                        JSONObject(response)
                    var msg = ""
                    jsonObject.optJSONObject("data")?.let {
                        msg = it.optString("message")

                    }
                    errorMsg.value = msg

                }else{
                    errorMsg.value = e.message
                }
            }
        }

    }

}