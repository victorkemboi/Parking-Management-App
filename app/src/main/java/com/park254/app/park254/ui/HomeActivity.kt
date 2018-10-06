package com.park254.app.park254.ui

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.R.styleable.NavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.ui.fragments.AttendantFragment
import com.park254.app.park254.ui.fragments.HomeFragment
import com.park254.app.park254.ui.fragments.MyPlacesFragment
import com.park254.app.park254.ui.fragments.OwnerFragment
import com.park254.app.park254.ui.repo.HomeViewModel
import com.park254.app.park254.utils.UtilityClass.REQUEST_CHECK_SETTINGS
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.toolbar_2.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity(),
        MyPlacesFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        OwnerFragment.OnFragmentInteractionListener,
        AttendantFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener,
         View.OnClickListener{


    @Inject
    lateinit var viewModel:HomeViewModel

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

        initToolBar("Home")

        initNavigationMenu()

        val fragmentClass: Class<*> =   HomeFragment::class.java

        viewModel.homeMapFragment = fragmentClass.newInstance() as Fragment
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.mainHomeContentFrameLayout, viewModel.homeMapFragment!!).commit()
        //Log.d("User:", FirebaseAuth.getInstance().currentUser.toString())

        val headerView  = nav_view.getHeaderView(0)
        val navUsername   = headerView.findViewById(R.id.user_name) as TextView
        navUsername.text = FirebaseAuth.getInstance().currentUser!!.displayName
        val navEmail = headerView.findViewById<TextView>(R.id.email)
        navEmail.text = FirebaseAuth.getInstance().currentUser!!.email
        val userAvatar = headerView.findViewById<ImageView>(R.id.avatar)
        Glide.with(this).load(FirebaseAuth.getInstance().currentUser!!.photoUrl).into(userAvatar)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CHECK_SETTINGS){
            viewModel.homeMapFragment?.onActivityResult(requestCode, resultCode, data)
        }else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onFragmentInteraction(uri: Uri) {
        // TODO Implement
    }

     private fun initToolBar(title:String){
        toolbar2.setNavigationIcon(R.drawable.ic_back_arrow)
       setSupportActionBar(toolbar2)

        supportActionBar!!.title = title
        supportActionBar?.apply { setDisplayHomeAsUpEnabled(false)
            setHomeAsUpIndicator(R.drawable.ic_menu)}

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

    private fun selectDrawerItem(menuItem: MenuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        if (menuItem.itemId == R.id.nav_bookings){
            menuItem.isChecked = true
            startActivity(
                    Intent(this@HomeActivity, BookingsActivity::class.java))
        }
        if (menuItem.itemId == R.id.nav_payments){

            menuItem.isChecked = true
            startActivity(
                    Intent(this@HomeActivity, PaymentsActivity::class.java))

        }
        if(menuItem.itemId ==R.id.nav_log_out){


            // Initialize a new instance of
            val builder = AlertDialog.Builder(this@HomeActivity)
            val titleInActionBar= supportActionBar!!.title

            // Set the alert dialog title
            builder.setTitle("Log Out")

            // Display a message on alert dialog
            builder.setMessage("Are you want to log out?")

            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("YES"){dialog, which ->
                // Do something when user press the positive button

                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                finishAffinity()
                finish()

            }

            // Display a negative button on alert dialog
            builder.setNegativeButton("No"){dialog,which ->

                activity_main
                initToolBar(titleInActionBar as String)
                initNavigationMenu()
            }
            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()

        }

        //TODO:  fragment variables in view model

        var fragment: Fragment? = null
        val fragmentClass: Class<*> = when (menuItem.itemId) {
            R.id.nav_owner -> OwnerFragment::class.java
            R.id.nav_attendant -> AttendantFragment::class.java
            R.id.nav_my_places -> MyPlacesFragment::class.java

            else -> HomeFragment::class.java
        }

        if (menuItem.itemId == R.id.nav_home){
            fragment= viewModel.homeMapFragment
        }else{
            try {
                fragment = fragmentClass.newInstance() as Fragment
            } catch (e: Exception) {
                e.printStackTrace()
            }
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
    fun getActiveFragment(): String{
        val fragments = supportFragmentManager.fragments
        if(fragments.isNotEmpty()) {
          val activeFragment = fragments[fragments.size-1]
        }

        return  ""
    }






}
