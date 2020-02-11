package com.nencer.nencerwallet.ext

import com.nencer.nencerwallet.config.Settings

fun isLogin(): Boolean = !Settings.Account.token.isNullOrBlank()
fun isPayment(): Boolean = Settings.Payment.isPayment

