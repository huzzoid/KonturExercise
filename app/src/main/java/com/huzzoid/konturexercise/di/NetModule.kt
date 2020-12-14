package com.huzzoid.konturexercise.di

import com.huzzoid.konturexercise.data.net.Net
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetModule {

    @Provides
    fun providesOkHttp() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    @Provides
    fun providesRetrofit(httpClient: OkHttpClient): Net = Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/SkbkonturMobile/mobile-test-droid/master/json/")
        .client(httpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Net::class.java)
}