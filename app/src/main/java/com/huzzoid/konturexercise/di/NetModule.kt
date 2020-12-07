package com.huzzoid.konturexercise.di

import android.app.Application
import com.huzzoid.konturexercise.data.net.Net
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val MAX_CACHE_SIZE = 1 * 1024 * 1024L
private const val MAX_CACHE_AGE = 1

@Module
@InstallIn(SingletonComponent::class)
object NetModule {

    @Provides
    fun providesOkHttp(application: Application) = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        })
        .addNetworkInterceptor(Interceptor {
            val response: Response = it.proceed(it.request())
            val cacheControl: CacheControl = CacheControl.Builder()
                .maxAge(MAX_CACHE_AGE, TimeUnit.MINUTES)
                .build()
            response.newBuilder()
                .removeHeader("Cache-Control")
                .header("Cache-Control", cacheControl.toString())
                .build()
        })
        .cache(Cache(application.cacheDir, MAX_CACHE_SIZE))
        .build()

    @Provides
    fun providesRetrofit(httpClient: OkHttpClient): Net = Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/SkbkonturMobile/mobile-test-droid/master/json/")
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Net::class.java)
}