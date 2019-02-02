package com.park254.app.park254.ui

import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.models.Employee
import com.park254.app.park254.models.LotResponse
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.ui.fragments.AttendantFragment
import com.park254.app.park254.ui.fragments.HomeFragment
import com.park254.app.park254.ui.fragments.MyPlacesFragment
import com.park254.app.park254.ui.fragments.OwnerFragment
import com.park254.app.park254.ui.repo.HomeViewModel
import com.park254.app.park254.utils.SharedPrefs
import com.park254.app.park254.utils.UtilityClass.REQUEST_CHECK_SETTINGS
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.toolbar_2.*
import javax.inject.Inject
import dagger.android.AndroidInjection


class HomeActivity : AppCompatActivity(),
        MyPlacesFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener,
        OwnerFragment.OnFragmentInteractionListener,
        AttendantFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener,
        View.OnClickListener {

    @Inject
    lateinit var viewModel: HomeViewModel

    @Inject
    lateinit var retrofitApiService: RetrofitApiService

    @Inject
    lateinit var settings : SharedPrefs


    override fun onClick(p0: View?) {

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        selectDrawerItem(item)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        // initToolbar()
       // (application as App).applicationInjector.inject(this)
        AndroidInjection.inject(this)
        initToolBar("Home")

        initNavigationMenu()
       // setNavDrawerMenuOwnerAndAttendant()
        val fragmentClass: Class<*> = HomeFragment::class.java

        viewModel.homeMapFragment = fragmentClass.newInstance() as Fragment
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.mainHomeContentFrameLayout, viewModel.homeMapFragment!!).commit()
        //Log.d("User:", FirebaseAuth.getInstance().currentUser.toString())

        val headerView = nav_view.getHeaderView(0)
        val navUsername = headerView.findViewById(R.id.user_name) as TextView
        navUsername.text = FirebaseAuth.getInstance().currentUser!!.displayName
        val navEmail = headerView.findViewById<TextView>(R.id.email)
        navEmail.text = FirebaseAuth.getInstance().currentUser!!.email
        val userAvatar = headerView.findViewById<ImageView>(R.id.avatar)
        Glide.with(this).load(FirebaseAuth.getInstance().currentUser!!.photoUrl).into(userAvatar)


        Log.w("Token: ",settings.token)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            viewModel.homeMapFragment?.onActivityResult(requestCode, resultCode, data)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onFragmentInteraction(uri: Uri) {
        // TODO Implement
    }

     private fun initToolBar(title: String) {
        toolbar2.setNavigationIcon(R.drawable.ic_back_arrow)
        setSupportActionBar(toolbar2)

        setToolbarTitle(title)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

    }


    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        setNavDrawerMenuOwnerAndAttendant()
        return super.onPrepareOptionsMenu(menu)

    }

    fun setToolbarTitle(title: String){
        supportActionBar!!.title = title
    }

    private fun initNavigationMenu() {


        val drawerToggle = object : ActionBarDrawerToggle(this, drawer_layout, toolbar2, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

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
        if (menuItem.itemId == R.id.nav_bookings) {
            menuItem.isChecked = true
            startActivity(
                    Intent(this@HomeActivity, BookingsActivity::class.java))
        }
        if (menuItem.itemId == R.id.nav_payments) {

            menuItem.isChecked = true
            startActivity(
                    Intent(this@HomeActivity, PaymentsActivity::class.java))

        }

        if (menuItem.itemId == R.id.nav_become_owner){

                startActivity(
                        Intent(
                                this,ParkingLotRegistrationActivity::class.java
                        )
                )

        }
        if (menuItem.itemId == R.id.nav_log_out) {


            // Initialize a new instance of
            val builder = AlertDialog.Builder(this@HomeActivity)
            val titleInActionBar = supportActionBar!!.title

            // Set the alert dialog title
            builder.setTitle("Log Out")

            // Display a message on alert dialog
            builder.setMessage("Are you want to log out?")

            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("YES") { dialog, which ->
                // Do something when user press the positive button

                FirebaseAuth.getInstance().signOut()
                settings.userId = ""
                settings.token = ""
                settings.owner= false
                settings.attendant = false
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                finishAffinity()
                finish()

            }

            // Display a negative button on alert dialog
            builder.setNegativeButton("No") { dialog, which ->

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

        if (menuItem.itemId == R.id.nav_home) {
            fragment = viewModel.homeMapFragment
        } else {
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

    private fun setNavDrawerMenuOwnerAndAttendant(){

        isAttendant()
        isOwner()
    }

   private fun isOwner(){
       if (settings.owner){
           nav_view.menu.findItem(R.id.nav_owner).isVisible = true
           nav_view.menu.findItem(R.id.nav_become_owner).isVisible = false
       }else {
           retrofitApiService.getOwnedParkingLots().observe(
                   this, Observer<ApiResponse<List<LotResponse>>> { lots ->
               run {
                   if (lots != null) {
                       if (lots.isSuccessful) {
                           // Log.w("isOwner:",lots.body?.isNotEmpty().toString())
                           if (lots.body != null) {
                               settings.owner = lots.body.isNotEmpty()
                               nav_view.menu.findItem(R.id.nav_owner).isVisible = lots.body.isNotEmpty()
                               if (nav_view.menu.findItem(R.id.nav_owner).isVisible) {
                                   nav_view.menu.findItem(R.id.nav_become_owner).isVisible = false
                               }
                           }

                       }
                   }
               }
           }
           )
       }
    }

    private fun isAttendant(){

        if (settings.attendant){
            nav_view.menu.findItem(R.id.nav_attendant).isVisible = true
        }else{
            retrofitApiService.getEmployeeByUserId(settings.userId!!).observe(this, Observer<ApiResponse<Employee>> { response ->
                run {
                    if (response != null) {
                        if (response.isSuccessful) {
                            settings.attendant = response.body != null
                            nav_view.menu.findItem(R.id.nav_attendant).isVisible  = response.body != null

                        }
                    }
                }
            }
            )
        }



    }


}
