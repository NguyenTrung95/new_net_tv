package com.eliving.tv.di

import android.util.Log
import com.eliving.tv.BuildConfig
import com.eliving.tv.data.http.interceptor.AuthorizationInterceptor
import com.eliving.tv.service.live.api.LiveApi
import com.eliving.tv.service.live.repository.LiveRepository
import com.eliving.tv.service.live.viewmodel.LiveViewModel
import com.eliving.tv.service.radio.api.RadioApi
import com.eliving.tv.service.radio.repository.RadioRepository
import com.eliving.tv.service.radio.viewmodel.RadioViewModel
import com.eliving.tv.service.user.api.UserApi
import com.eliving.tv.service.user.repository.UserRepository
import com.eliving.tv.service.user.viewmodel.UserViewModel
import com.eliving.tv.service.vod.api.VODApi
import com.eliving.tv.service.vod.repository.VODRepository
import com.eliving.tv.service.vod.viewmodel.VODViewModel

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


private const val BASE_URL = "http://api.ttvmax.com/"
private const val TIME_OUT = 30L
private const val TAG = "fmt"

val viewModelModule = module {
    viewModel { RadioViewModel(get()) }
    viewModel { LiveViewModel(get()) }
    viewModel { UserViewModel(get()) }
    viewModel { VODViewModel(get()) }


}

val reposModule = module {
    factory { RadioRepository(get()) }
    factory { LiveRepository(get()) }
    factory { UserRepository(get()) }
    factory { VODRepository(get()) }

}

val remoteModule = module {

    single {
        val httpLoggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.e(TAG, message)
            }
        })
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpLoggingInterceptor
    }

    single {
        val builder = OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor())
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)

            .addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                    val request = chain.request()
                    val response = chain.proceed(request)
                    return response
                }
            })
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(get<HttpLoggingInterceptor>())
        }
        builder.build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder()
                    .setLenient()
                    .create()))
            .client(get<OkHttpClient>())
            .baseUrl(BASE_URL)
            .build()
    }
    single<VODApi> { get<Retrofit>().create(VODApi::class.java) }
    single<RadioApi> { get<Retrofit>().create(RadioApi::class.java) }
    single<LiveApi> { get<Retrofit>().create(LiveApi::class.java) }
    single<UserApi> { get<Retrofit>().create(UserApi::class.java) }



}

val localModule = module {
    /*single {
        Room.databaseBuilder(AppContext, AppDataBase::class.java, DB_NAME).build()
    }
    single { get<AppDataBase>().getUserDao() }*/
}

val appModule = listOf(viewModelModule, reposModule, remoteModule, localModule)