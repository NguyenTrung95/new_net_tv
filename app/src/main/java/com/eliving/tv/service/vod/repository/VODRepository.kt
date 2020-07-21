package com.eliving.tv.service.vod.repository

import com.eliving.tv.service.vod.api.VODApi

class VODRepository(private val api: VODApi) {

    /*getVODCategory*/
    suspend fun getVODCategory() = api.getVodCategory()


    /*getVOD*/
    suspend fun getVOD(category_id: String, page: Int) =
        api.getVOD(category_id, page)


}
