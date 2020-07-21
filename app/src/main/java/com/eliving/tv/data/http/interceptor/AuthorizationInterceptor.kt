package com.eliving.tv.data.http.interceptor
import com.eliving.tv.base.constant.Constant
import com.eliving.tv.config.Settings
import com.eliving.tv.ext.isAuth
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        return chain.proceed(request.newBuilder().apply {
            when {
                isAuth() -> {
                    val auth = "Bearer ${Settings.Account.token}"
                    addHeader("Content-Type", "application/json")
                    addHeader("Accept","application/json")
                    addHeader(Constant.AUTHORIZATION, auth)
                }
                else -> {
                    addHeader("Content-Type", "application/json")
                    addHeader("Accept","application/json")
                }
            }

        }.build())

    }
}