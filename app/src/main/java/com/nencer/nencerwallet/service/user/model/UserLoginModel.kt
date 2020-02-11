package com.nencer.nencerwallet.service.user.model

import androidx.databinding.ObservableField

data class UserLoginModel(
    val username: ObservableField<String> = ObservableField(""),
    val password: ObservableField<String> = ObservableField("")
)