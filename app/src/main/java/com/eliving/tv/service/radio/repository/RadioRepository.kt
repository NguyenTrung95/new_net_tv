package com.eliving.tv.service.radio.repository

import com.eliving.tv.service.radio.api.RadioApi

class RadioRepository(private val api: RadioApi) {

    /*get radios*/
    suspend fun getRadios() = api.getRadios()


}
