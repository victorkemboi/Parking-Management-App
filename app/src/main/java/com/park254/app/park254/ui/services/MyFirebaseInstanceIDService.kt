package com.park254.app.park254.ui.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceIdService
import com.park254.app.park254.utils.SharedPrefs
import javax.inject.Inject


class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {
    @Inject
    lateinit var settings: SharedPrefs

    @Inject
    lateinit var firebaseAuth:FirebaseAuth




    override fun onTokenRefresh() {
        firebaseAuth.getAccessToken(true).addOnCompleteListener {
            task2 ->
            run {
                if (task2.isSuccessful) {
                    val token = task2.result.token
                    settings.token = token

                   // Log.d("Sign In jwt token", token   )


                }
            }
        }

        // Get updated InstanceID token.

    }

}