package com.park254.app.park254.ui

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.gms.flags.impl.SharedPreferencesFactory
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.models.User
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.ui.fragments.AttendantFragment
import com.park254.app.park254.ui.fragments.MainHomeFragment
import com.park254.app.park254.ui.fragments.OwnerFragment
import com.park254.app.park254.ui.repo.HomeViewModel
import com.park254.app.park254.utils.SharedPrefs
import com.park254.app.park254.utils.UtilityClass
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import com.schibstedspain.leku.LATITUDE
import com.schibstedspain.leku.LOCATION_ADDRESS
import com.schibstedspain.leku.LONGITUDE
import com.schibstedspain.leku.LocationPickerActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.drawer_header_layout.*
import kotlinx.android.synthetic.main.include_cardview_search_bar.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar_2.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity(),
        MainHomeFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        OwnerFragment.OnFragmentInteractionListener,
        AttendantFragment.OnFragmentInteractionListener,
         View.OnClickListener{
    @Inject
    lateinit var viewModel:HomeViewModel
    @Inject
    lateinit var retrofitApiService: RetrofitApiService

    @Inject
    lateinit var settings: SharedPrefs


    override fun onClick(p0: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        selectDrawerItem(item)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
       // initToolbar()
        (application as App).applicationInjector.inject(this)



        toolbar2.setNavigationIcon(R.drawable.ic_back_arrow)
        setSupportActionBar(toolbar2)

        supportActionBar!!.title = "Home"
        supportActionBar?.apply { setDisplayHomeAsUpEnabled(false)
            setHomeAsUpIndicator(R.drawable.ic_menu)}

        initNavigationMenu()




        var fragment: Fragment? = null
        val fragmentClass: Class<*> =   MainHomeFragment::class.java
        fragment = fragmentClass.newInstance() as Fragment
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.mainHomeContentFrameLayout, fragment).commit()

        Log.w("Trying to login",settings.token)

        retrofitApiService.registerUser().observe(this, Observer<ApiResponse<User>> {
            response->
            if (response != null) {

                Log.d("Resp",response.body.toString())
                //  response.body
                // Log.d("Fire auth", FirebaseInstanceId.getInstance().token)
            }


        })

        //Log.d("User:", FirebaseAuth.getInstance().currentUser.toString())
        val headerView  = nav_view.getHeaderView(0)
        val navUsername   = headerView.findViewById(R.id.user_name) as TextView
        navUsername.text = FirebaseAuth.getInstance().currentUser!!.displayName
        val navEmail = headerView.findViewById<TextView>(R.id.email)
        navEmail.text = FirebaseAuth.getInstance().currentUser!!.email
        val userAvatar = headerView.findViewById<ImageView>(R.id.avatar)
        Glide.with(this).load(FirebaseAuth.getInstance().currentUser!!.photoUrl).into(userAvatar)








    }

    override fun onFragmentInteraction(uri: Uri) {
        // TODO Implement
    }







    private fun initNavigationMenu() {


        val drawerToggle = object : ActionBarDrawerToggle(this, drawer_layout, toolbar2, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {

        }
        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        nav_view.setNavigationItemSelectedListener { item ->
            selectDrawerItem(item)

            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }



    }


    fun selectDrawerItem(menuItem: MenuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        var fragment: Fragment? = null
        val fragmentClass: Class<*> = when (menuItem.itemId) {
            R.id.nav_owner -> OwnerFragment::class.java
            R.id.nav_attendant -> AttendantFragment::class.java
            else -> MainHomeFragment::class.java
        }

        try {
            fragment = fragmentClass.newInstance() as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Insert the fragment by replacing any existing fragment
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.mainHomeContentFrameLayout, fragment!!).commit()

        // Highlight the selected item has been done by NavigationView
        menuItem.isChecked = true
        // Set action bar title
        //title = menuItem.title
        supportActionBar!!.title = menuItem.title
        // Close the navigation drawer
        drawer_layout.closeDrawers()


    }



}
