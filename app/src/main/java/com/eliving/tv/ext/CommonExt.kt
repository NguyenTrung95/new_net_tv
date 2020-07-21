package com.eliving.tv.ext

import com.eliving.tv.config.Settings

fun isLogin(): Boolean = !Settings.Account.token.isNullOrBlank()
fun isAuth(): Boolean = Settings.Auth.isAuth

