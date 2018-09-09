package com.park254.app.park254.ui

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.graphics.PorterDuff
import android.location.Address
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.ui.fragments.LotRegistrationStepOneFragment
import com.park254.app.park254.ui.fragments.LotRegistrationStepTwoFragment
import com.park254.app.park254.ui.repo.ParkingLotRegistrationViewModel
import com.schibstedspain.leku.*
import kotlinx.android.synthetic.main.activity_parking_lot_registration.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject
import com.park254.app.park254.utils.UtilityClass
import kotlinx.android.synthetic.main.fragment_lot_registration_step_one.*
import android.widget.TextView




class ParkingLotRegistrationActivity : AppCompatActivity() ,
        LotRegistrationStepOneFragment.OnFragmentInteractionListener,
        LotRegistrationStepTwoFragment.OnFragmentInteractionListener
            {


                @Inject
    lateinit var viewModel: ParkingLotRegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_lot_registration)

        App.applicationInjector.inject(this)
        initToolbar()
        initComponent()





    }
                override fun onFragmentInteraction(uri: Uri) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }


     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            Log.d("RESULT****", "OK")
            if (requestCode == viewModel.MAP_BUTTON_REQUEST_CODE) {
                viewModel.lot.latitude = data.getDoubleExtra(LATITUDE, 0.0)
                Log.d("LATITUDE****", viewModel.lot.latitude.toString())
                viewModel.lot.longitude = data.getDoubleExtra(LONGITUDE, 0.0)
                Log.d("LONGITUDE****", viewModel.lot.longitude.toString())
                val address = data.getStringExtra(LOCATION_ADDRESS)
                Log.d("ADDRESS****", address.toString())
                val postalcode = data.getStringExtra(ZIPCODE)
                Log.d("POSTALCODE****", postalcode.toString())
                val bundle = data.getBundleExtra(TRANSITION_BUNDLE)
                Log.d("BUNDLE TEXT****", bundle.getString("test"))
                val fullAddress = data.getParcelableExtra<Address>(ADDRESS)
                if (fullAddress != null) {
                    Log.d("FULL ADDRESS****", fullAddress.toString())
                }
                val timeZoneId = data.getStringExtra(TIME_ZONE_ID)
                Log.d("TIME ZONE ID****", timeZoneId)
                val timeZoneDisplayName = data.getStringExtra(TIME_ZONE_DISPLAY_NAME)
                Log.d("TIME ZONE NAME****", timeZoneDisplayName)
            } else if (requestCode == 2) {
                viewModel.lot.latitude = data.getDoubleExtra(LATITUDE, 0.0)
                Log.d("LATITUDE****", viewModel.lot.latitude.toString())
                val longitude = data.getDoubleExtra(LONGITUDE, 0.0)
                Log.d("LONGITUDE****", viewModel.lot.longitude.toString())
                val address = data.getStringExtra(LOCATION_ADDRESS)
                Log.d("ADDRESS****", address.toString())
                val lekuPoi = data.getParcelableExtra<LekuPoi>(LEKU_POI)
                Log.d("LekuPoi****", lekuPoi.toString())
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("RESULT****", "CANCELLED")
        }
    }


    private fun initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Registration"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }


    private fun initComponent() {

        lyt_back.setOnClickListener {
            backStep(viewModel.current_step)
            bottomProgressDots(viewModel.current_step)
        }

        lyt_next.setOnClickListener {
            nextStep(viewModel.current_step)
            bottomProgressDots(viewModel.current_step)
        }

        bottomProgressDots(viewModel.current_step)
        lyt_back.visibility = View.INVISIBLE
        progressStepper(viewModel.current_step)
    }

    private fun nextStep(progressActive: Int) {
        if (progressActive < viewModel.MAX_STEP) {
            viewModel.previous_step = progressActive


                if (progressActive == 1) {
                    if (validateFirtSetInputs()) {

                       navigateNextStepper(progressActive)
                    }
                } else {
                    navigateNextStepper(progressActive)
                }




          //  ViewAnimation.fadeOutIn(status)
        }


    }
    fun navigateNextStepper(progress:Int){
        var current  = progress
        current++
        viewModel.current_step = current
        if (viewModel.current_step>1){
            lyt_back.visibility = View.VISIBLE
        }else{
            lyt_back.visibility = View.INVISIBLE
        }
        if (progress == viewModel.MAX_STEP){
            lyt_next_txt_view.text = "FINISH"
        }

        progressStepper(viewModel.current_step)
    }
    fun progressStepper(current:Int)
    {
        Log.d("Fragment","Enter Stepper")
        var fragment: Fragment? = null
        val fragment_tag: String = when(current){
            1->"STEP_ONE"

            2-> "STEP_TWO"

            else -> "FAILED"


        }

        val fragmentClass: Class<*> = when(current){
            1->LotRegistrationStepOneFragment::class.java
            2->LotRegistrationStepTwoFragment::class.java
           else->LotRegistrationStepOneFragment::class.java
        }


        try {

            fragment = fragmentClass.newInstance() as Fragment
            Log.d("Fragment","Setup done")

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Fragment","Setup failed")
        }

        // Insert the fragment by replacing any existing fragment
        val fragmentManager = supportFragmentManager



                if (viewModel.previous_step < viewModel.current_step) {

                    val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                            .replace(R.id.lotRegistrationContentFrameLayout, fragment!!, fragment_tag)
                            .addToBackStack(fragment_tag).commit()

                    viewModel.visible_fragment = fragment
                } else {
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                            .replace(R.id.lotRegistrationContentFrameLayout, fragment!!, fragment_tag)
                            .addToBackStack(fragment_tag)
                            .commit()

                    viewModel.visible_fragment = fragment
                }











    }



    private fun backStep(progressActive: Int) {
        var progress = progressActive

        if (progress > 1) {
            if (progress == viewModel.MAX_STEP){
                lyt_next_txt_view.text = "NEXT"
            }
            viewModel.previous_step = progress

            progress--
            viewModel.current_step = progress
            if (progress>1){

                lyt_back.visibility = View.VISIBLE
            }else if(progress == 1){
                lyt_back.visibility = View.INVISIBLE
            }
            progressStepper(progress)
            //ViewAnimation.fadeOutIn(status)
        }
    }

    private fun bottomProgressDots(current_active_index: Int) {
        var current_index = current_active_index
        current_index--
        val dotsLayout = findViewById<LinearLayout>(R.id.layoutDots)
        val dots = arrayOfNulls<ImageView>(viewModel.MAX_STEP)

        dotsLayout.removeAllViews()
        for (i in dots.indices) {
            dots[i] = ImageView(this)
            val widthHeight = 15
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams(widthHeight, widthHeight))
            params.setMargins(10, 10, 10, 10)
            dots[i]!!.layoutParams = params
            dots[i]!!.setImageResource(R.drawable.shape_circle)
            dots[i]!!.setColorFilter(resources.getColor(R.color.grey_20), PorterDuff.Mode.SRC_IN)
            dotsLayout.addView(dots[i])
        }

        if (dots.isNotEmpty()) {
            dots[current_index]!!.setImageResource(R.drawable.shape_circle)
            dots[current_index]!!.setColorFilter(resources.getColor(R.color.colorAccentDark2), PorterDuff.Mode.SRC_IN)
        }
    }

    fun validateFirtSetInputs():Boolean {




        val tv_parking_name = findViewById<TextView>(R.id.input_parking_lot_name)
        Log.d("validateFirtSetInputs",tv_parking_name.text.toString())
        if (tv_parking_name.text.toString().trim { it <= ' ' }.isEmpty()) run {

            input_layout_parking_lot_name.error = "Enter parking lot name!"
            UtilityClass.requestFocus(input_parking_lot_name, window)
            return false
        }
        if (input_street_name.text.toString().trim { it <= ' ' }.isEmpty()) run {
            input_layout_street_name.error = "Enter street name!"
            UtilityClass.requestFocus(input_layout_street_name,window)
            //return false
        }
        if (viewModel.lot.longitude == 0.0 && viewModel.lot.latitude == 0.0) run {
            input_layout_location.error = "Select Location!"
            UtilityClass.requestFocus(input_layout_street_name, window)
           // return false
        }


        return true
    }
}
