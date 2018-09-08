package com.park254.app.park254.ui

import android.app.ActionBar
import android.app.Activity
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.park254.app.park254.R
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.toolbar.*

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        val actionBar: ActionBar? = actionBar
        actionBar?.title = "Park 254"
        actionBar?.apply { setDisplayHomeAsUpEnabled(false)
            setHomeAsUpIndicator(R.drawable.ic_menu)}

        initNavigationMenu()
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
