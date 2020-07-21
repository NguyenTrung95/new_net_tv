package com.eliving.tv.config

import com.eliving.tv.base.constant.Constant
import com.eliving.tv.data.storage.Preference

object Settings {

    object AppInfo {
        var url by Preference(Constant.URL, "")
        var appLogo by Preference(Constant.LOGO_URL, "")
    }

    object Account {
        var id by Preference(Constant.USER_ID, "")
        var token by Preference(Constant.USER_TOKEN_API, "")
        var userName by Preference(Constant.USER_NAME, "")
        var password by Preference(Constant.USER_PASSWORD, "")
        var currencyCode by Preference(Constant.CURRENCY_CODE, "")
    }

    object Auth{
        var isAuth by Preference(Constant.IS_AUTH,false)
    }
}