package com.park254.app.park254.ui.driver

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.park254.app.park254.R
import kotlinx.android.synthetic.main.toolbar.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initToolbar()



    }

    private fun initToolbar() {
        //toolbar.setNavigationIcon(R.drawable.park_logo)
        //toolbar.setLogo(R.drawable.park_logo)
        setSupportActionBar(toolbar)

       supportActionBar!!.setTitle("")
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }




}
