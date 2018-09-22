package com.park254.app.park254.ui

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.park254.app.park254.App
import com.park254.app.park254.models.User
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.utils.SharedPrefs
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import javax.inject.Inject


class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var settings: SharedPrefs
    @Inject
    lateinit var retrofitApiService: RetrofitApiService

    @Inject
    lateinit var firebaseAuth:FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as App).applicationInjector.inject(this)
        startMain()
        finish()
    }


     private fun startMain() {

        if (firebaseAuth.currentUser != null) {

          firebaseAuth.addAuthStateListener { auth ->run{
              val mUser = auth.currentUser
              mUser?.getIdToken(true)?.addOnCompleteListener { task ->
                  if (task.isSuccessful) {
                      val idToken = task.result.token
                      // Log.w("User getToken: ", idToken)
                      settings.token = idToken

                      startActivity(Intent(this@SplashActivity, HomeActivity::class.java))


                  } else {
                      startLogin()
                  }
              } ?: startLogin()

          }
          }


        } else {

            startLogin()

        }



    }

    private fun startLogin(){
        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))

    }
}