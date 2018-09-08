package com.park254.app.park254.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth


class SplashActivity : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Start ic_home activity

        Log.d("mAuth",mAuth.currentUser.toString())
        //startActivity(
            //   Intent(this@SplashActivity, HomeActivity::class.java))
        // close splash activity
        startMain()
        finish()
    }


     fun startMain() {

        if (mAuth.currentUser != null) {
            val currentUser = mAuth.currentUser
            startActivity(
                    Intent(this@SplashActivity, HomeActivity::class.java))

        } else {

            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))

        }

    }
}