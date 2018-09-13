package com.park254.app.park254.di

import javax.inject.Singleton
import dagger.Provides
import android.app.Application
import android.preference.PreferenceManager
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.park254.app.park254.App
import com.park254.app.park254.network.FirebaseUserIdTokenInterceptor
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.utils.livedata_adapter.LiveDataCallAdapterFactory
import dagger.Module
import okhttp3.Cache
import okhttp3.Interceptor
import java.io.File
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit


@Module
class NetModule// Constructor needs one parameter to instantiate.
(val mBaseUrl: String) {

    // Dagger will only look for methods annotated with @Provides
    @Provides
    @Singleton
    internal fun providesSharedPreferences(application: App):
    // Application reference must come from AppModule.class
            SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
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
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.networkInterceptors().add(FirebaseUserIdTokenInterceptor())
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