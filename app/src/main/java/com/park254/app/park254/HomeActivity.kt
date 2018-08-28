package com.park254.app.park254

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import kotlinx.android.synthetic.main.toolbar.*

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
