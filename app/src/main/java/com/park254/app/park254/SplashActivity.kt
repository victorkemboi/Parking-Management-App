package com.park254.app.park254

import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Start ic_home activity
        startActivity(
                Intent(this@SplashActivity, HomeActivity::class.java))
        // close splash activity
        finish()
    }


    internal fun startMain() {
       /** if (settings.IsloggedIn() && settings.getUser() != null) {



        } else {

            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }   **/

    }
}