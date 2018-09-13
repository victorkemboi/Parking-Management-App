package com.park254.app.park254.network

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.park254.app.park254.utils.UtilityClass
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class FirebaseUserIdTokenInterceptor : Interceptor {

    @Throws(IOException::class)
    override
    fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        try {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                throw Exception("User is not logged in.")
            } else {
                val task = user.getIdToken(true)
                val tokenResult = Tasks.await(task)
                val idToken = tokenResult.token

                if (idToken == null) {
                    throw Exception("idToken is null")
                } else {
                    Log.d("Header Interceptor",idToken)
                    val modifiedRequest = request.newBuilder()
                            .addHeader(UtilityClass.X_FIREBASE_ID_TOKEN, " $idToken")
                            .build()
                    return chain.proceed(modifiedRequest)
                }
            }
        } catch (e: Exception) {
            Log.d("Header Interceptor",e.message)
            throw IOException(e.message)

        }

    }


}