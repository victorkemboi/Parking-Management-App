package com.park254.app.park254.di

import javax.inject.Singleton
import dagger.Provides
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.park254.app.park254.App
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.utils.SharedPrefs
import com.park254.app.park254.utils.livedata_adapter.LiveDataCallAdapterFactory
import dagger.Module
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

@Module
class NetModule// Constructor needs one parameter to instantiate.
(val mBaseUrl: String, val app:App) {


    @Provides
    @Singleton
    internal fun providesSharedPreferences(): SharedPrefs {
        return SharedPrefs(app)
    }
/*
    @Provides
    @Singleton
    internal fun provideOkHttpCache(): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        val httpCacheDirectory = File(app.applicationContext.cacheDir, "http-cache")
        return Cache(httpCacheDirectory, cacheSize.toLong())
    }
    */

    @Provides
    @Singleton
     internal fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(sharedPrefs: SharedPrefs): OkHttpClient {


        val client = OkHttpClient.Builder()
        client.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                    .addHeader("Authorization", String.format("Bearer %s", sharedPrefs.token))
                    .build()
            chain.proceed(request)
        }
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        client.addInterceptor(interceptor)
        client.connectTimeout(30, TimeUnit.SECONDS)
        client.readTimeout(30, TimeUnit.SECONDS)
        //client.cache(cache)
        return client.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): RetrofitApiService {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build().create(RetrofitApiService::class.java)
    }


}

// implementation "com.squareup.okhttp3:okhttp:$okhttp"
//implementation 'com.squareup.okhttp3:logging-interceptor:3.9.0'