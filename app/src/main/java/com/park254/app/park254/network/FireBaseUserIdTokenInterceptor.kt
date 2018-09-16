package com.park254.app.park254.network

import android.content.SharedPreferences
import android.util.Log
import com.google.android.gms.flags.impl.SharedPreferencesFactory.getSharedPreferences
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.park254.app.park254.App
import com.park254.app.park254.utils.UtilityClass
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject


class FirebaseUserIdTokenInterceptor
    constructor(val app: App): Interceptor {
    override
    fun intercept(chain: Interceptor.Chain): Response {
        val settings: SharedPreferences  = getSharedPreferences(app)
        val token = settings.getString("Token","")

        val request = chain.request()
        val modifiedRequest = request.newBuilder()
                    .addHeader("Authorization" , UtilityClass.X_FIREBASE_ID_TOKEN + " $token")
                    .build()
            return chain.proceed(modifiedRequest)



    }


}