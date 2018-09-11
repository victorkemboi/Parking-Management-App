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
import com.park254.app.park254.ui.fragments.LotRegistrationStepThreeFragment
import kotlinx.android.synthetic.main.fragment_lot_registration_step_two.*
import kotlinx.android.synthetic.main.toolbar_2.*


class ParkingLotRegistrationActivity : AppCompatActivity() ,
        LotRegistrationStepOneFragment.OnFragmentInteractionListener,
        LotRegistrationStepTwoFragment.OnFragmentInteractionListener,
        LotRegistrationStepThreeFragment.OnFragmentInteractionListener
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

         super.onActivityResult(requestCode,resultCode,data);
    }


    private fun initToolbar() {

        toolbar2.setNavigationIcon(R.drawable.ic_back_arrow)
        setSupportActionBar(toolbar2)

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
                    if (validateFirstSetInputs()) {
                        viewModel.lot.name = input_parking_lot_name.text.toString()
                        viewModel.lot.streetName = input_street_name.text.toString()
                        viewModel.lot.parkingSpaces = input_picker_parking_spaces.value


                       navigateNextStepper(progressActive)
                    }
                } else if(progressActive ==2)
                {
                    if (validateSecondSetInputs()) {
                        viewModel.lot.email = input_email.text.toString()
                        viewModel.lot.contactNumber =  Integer.parseInt(input_parking_lot_contact_no.text.toString())
                        viewModel.lot.paybillNumber = input_paybill_no.text.toString()
                        viewModel.rate.minimumTime = Integer.parseInt( input_min_time.text.toString() )
                        viewModel.rate.maximumTime = Integer.parseInt( input_max_time.text.toString() )
                        viewModel.rate.cost =   input_cost.text.toString().toDouble()

                        navigateNextStepper(progressActive)
                    }
                }

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

            else -> "THREE"


        }

        val fragmentClass: Class<*> = when(current){
            1->LotRegistrationStepOneFragment::class.java
            2->LotRegistrationStepTwoFragment::class.java
           else->LotRegistrationStepThreeFragment::class.java
        }


        try {

            fragment = fragmentClass.newInstance() as Fragment
            changeHeader(current)
           // Log.d("Fragment","Setup done")

        } catch (e: Exception) {
            e.printStackTrace()
           // Log.d("Fragment","Setup failed")
        }

        // Insert the fragment by replacing any existing fragment
        val fragmentManager = supportFragmentManager



                if (viewModel.previous_step < viewModel.current_step) {

                    val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                            .replace(R.id.lotRegistrationContentFrameLayout, fragment!!, fragment_tag)
                            .addToBackStack(fragment_tag).commit()

                } else {
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                            .replace(R.id.lotRegistrationContentFrameLayout, fragment!!, fragment_tag)
                            .addToBackStack(fragment_tag)
                            .commit()

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

    fun validateFirstSetInputs():Boolean {


       // Log.d("validateFirtSetInputs",input_parking_lot_name.text.toString())
        if (input_parking_lot_name.text.toString().trim { it <= ' ' }.isEmpty()) run {

            input_layout_parking_lot_name.error = "Enter parking lot name!"
            UtilityClass.requestFocus(input_parking_lot_name, window)
            return false
        }
        if (input_street_name.text.toString().trim { it <= ' ' }.isEmpty()) run {
            input_layout_street_name.error = "Enter street name!"
            UtilityClass.requestFocus(input_layout_street_name,window)
          return false
        }
        if (viewModel.lot.longitude == 0.0 && viewModel.lot.latitude == 0.0) run {
            input_layout_location.error = "Select Location!"
            UtilityClass.requestFocus(input_layout_street_name, window)
          return false
        }
        if (input_picker_parking_spaces.value == 1) run {
            input_layout_spaces.error = "Set spaces available!!"
            UtilityClass.requestFocus(input_layout_spaces, window)
            return false
        }


        return true
    }

     fun validateSecondSetInputs():Boolean {


                    if (input_email.text.toString().trim { it <= ' ' }.isEmpty()) run {

                        input_layout_email.error = "Enter Contact Email!"
                        UtilityClass.requestFocus(input_layout_email, window)
                        return false
                    }
                    if (input_parking_lot_contact_no.text.toString().trim { it <= ' ' }.isEmpty()) run {
                        input_layout_parking_lot_contact_no.error = "Enter contact number!"
                        UtilityClass.requestFocus(input_parking_lot_contact_no,window)
                       return false
                    }
                     if (input_paybill_no.text.toString().trim { it <= ' ' }.isEmpty()) run {
                         input_layout_paybill_no.error = "Enter paybill no!"
                         UtilityClass.requestFocus(input_layout_paybill_no,window)
                         return false
                     }
                     if (input_min_time.text.toString().trim { it <= ' ' }.isEmpty()) run {
                         input_layout_min_time.error = "Enter min time allowed!"
                         UtilityClass.requestFocus(input_layout_min_time,window)
                        return false
                     }
                     if (input_max_time.text.toString().trim { it <= ' ' }.isEmpty()) run {
                         input_layout_max_time.error = "Enter max time allowed!"
                         UtilityClass.requestFocus(input_layout_max_time,window)
                         return false
                     }
                     if (input_cost.text.toString().trim { it <= ' ' }.isEmpty()) run {
                         input_layout_cost.error = "Enter Cost per Hour!"
                         UtilityClass.requestFocus(input_layout_cost,window)
                         return false
                     }


                    return true
                }


     fun changeHeader(pos:Int){
        when(pos){
            1->txtRegHeader.text = "Basic Info"
            2->txtRegHeader.text = "Contact Info"
            3->txtRegHeader.text = "Photos"
        }
    }
}
