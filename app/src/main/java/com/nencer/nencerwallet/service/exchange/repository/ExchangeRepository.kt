package com.nencer.nencerwallet.service.exchange.repository

import com.nencer.nencerwallet.config.Configs
import com.nencer.nencerwallet.config.Settings
import com.nencer.nencerwallet.service.exchange.api.ExchangeApi

class ExchangeRepository(private val mApi: ExchangeApi) {

    /*charginglistcard*/
    suspend fun charginglistcard() =
        mApi.charginglistcard(Configs.MODE_KEY, Settings.Account.token, Settings.Account.id)

    /*chargingcardprice*/
    suspend fun chargingcardprice(telco_key: String, cardValue: String) = mApi.chargingcardprice(
        Configs.MODE_KEY,
        Settings.Account.token, Settings.Account.id, telco_key, cardValue
    )

    /*postchargingcard*/
    suspend fun postchargingcard(
        telco_key: String, card_serial: String, card_code: String,
        card_value: String
    ) = mApi.postchargingcard(
        Configs.MODE_KEY,
        Settings.Account.token, Settings.Account.id, telco_key, card_serial, card_code, card_value
    )

    /*charginghistory*/
    suspend fun charginghistory(
        per_page: Int,
        page: Int
    ) = mApi.charginghistory(
        Configs.MODE_KEY,
        Settings.Account.token,
        Settings.Account.id,
        per_page, page
    )


}
