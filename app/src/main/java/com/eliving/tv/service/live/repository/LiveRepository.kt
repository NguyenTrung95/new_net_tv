package com.eliving.tv.service.live.repository

import com.eliving.tv.service.live.api.LiveApi
import kotlinx.android.synthetic.main.activity_chanels.*
import java.util.*

class LiveRepository(private val api: LiveApi) {

    /*getCatchup*/
    suspend fun getCatchup(channel: String,date: String) =
        api.getCatchup(channel,date)
    /*getLive*/
    suspend fun getLive(id: String) =
        api.getLive(id)

    /*getEpgInfor*/
    suspend fun getEpgInfor(id: String) =
        api.getEpgInfor(id)

    /*getEpgInfor*/
    suspend fun getLiveCategory() =
        api.getLiveCategory()

    suspend fun getLiveChannel() =
        api.getLiveChannel()

    suspend fun getEpgDate() =
        api.getDateEpg()

    suspend fun getCatchupDate() =
        api.getDateCatchup()

    suspend fun getEpg(channel: String,date: String) =
        api.getEpg(channel,date)
}
