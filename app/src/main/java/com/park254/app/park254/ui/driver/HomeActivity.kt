package com.park254.app.park254.ui.driver

import android.app.ActionBar
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.park254.app.park254.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
       // initToolbar()


        val actionBar: ActionBar? = supportActionBar as ActionBar?
       actionBar?.title = "Park 254"
        actionBar?.apply { setDisplayHomeAsUpEnabled(false)
        setHomeAsUpIndicator(R.drawable.ic_menu)}

        initNavigationMenu()


    }

    private fun initToolbar() {
        //toolbar.setNavigationIcon(R.drawable.park_logo)
        //toolbar.setLogo(R.drawable.park_logo)
        setSupportActionBar(toolbar)

       supportActionBar!!.setTitle("Park 254")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)


    }


    private fun initNavigationMenu() {


        val drawerToggle = object : ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {   override
            fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        }
        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        nav_view.setNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.nav_home -> drawer_layout.closeDrawer(GravityCompat.START)
            }

            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }




        // open drawer at start
        //drawer_layout.openDrawer(GravityCompat.START)
    }






}
