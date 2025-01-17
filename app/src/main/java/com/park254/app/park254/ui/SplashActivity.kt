package com.park254.app.park254.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.park254.app.park254.App
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.utils.SharedPrefs
import dagger.android.AndroidInjection
import javax.inject.Inject


class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var settings: SharedPrefs
    @Inject
    lateinit var retrofitApiService: RetrofitApiService

    @Inject
    lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidInjection.inject(this)
        startMain()

    }


    private fun startMain() {

        if(settings.userId != ""){
            firebaseAuth.addAuthStateListener { auth ->
                run {
                    val mUser = auth.currentUser
                    mUser?.getIdToken(true)?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val idToken = task.result?.token
                            // Log.w("User getToken: ", idToken)
                            settings.token = idToken

                        }
                    }

                }
            }
            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            finish()
        }else{
            startLogin()
        }



    }

    private fun startLogin() {
        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))

        finish()
    }
}