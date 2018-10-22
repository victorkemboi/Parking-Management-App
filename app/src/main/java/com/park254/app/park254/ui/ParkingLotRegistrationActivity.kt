package com.park254.app.park254.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import kotlinx.android.synthetic.main.activity_parking_lot_registration.*
import javax.inject.Inject
import com.park254.app.park254.utils.UtilityClass
import kotlinx.android.synthetic.main.fragment_lot_registration_step_one.*
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.ui.fragments.LotRegistrationStepThreeFragment
import kotlinx.android.synthetic.main.fragment_lot_registration_step_three.*
import kotlinx.android.synthetic.main.fragment_lot_registration_step_two.*
import kotlinx.android.synthetic.main.toolbar_2.*
import android.arch.lifecycle.Observer
import android.content.Context
import android.util.Base64
import com.park254.app.park254.models.Lot
import com.park254.app.park254.models.LotImage
import com.park254.app.park254.models.LotResponse
import com.park254.app.park254.models.Rate
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import dagger.android.AndroidInjection
import kotlinx.coroutines.experimental.*
import java.io.ByteArrayOutputStream
import kotlin.coroutines.experimental.CoroutineContext


class ParkingLotRegistrationActivity : AppCompatActivity() ,
        CoroutineScope,
        LotRegistrationStepOneFragment.OnFragmentInteractionListener,
        LotRegistrationStepTwoFragment.OnFragmentInteractionListener,
        LotRegistrationStepThreeFragment.OnFragmentInteractionListener
            {

     @Inject
    lateinit var viewModel: ParkingLotRegistrationViewModel

    @Inject
    lateinit var retrofitApiService: RetrofitApiService

    @Inject
    lateinit var job: Job

    @Inject
    lateinit var threadPool : ExecutorCoroutineDispatcher

    val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_lot_registration)

        AndroidInjection.inject(this)

        initToolbar()
        initComponent()





    }

    override fun onFragmentInteraction(uri: Uri) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

    override fun onBackPressed() {
        if (viewModel.current_step ==1){
            finish()
        }else{
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        viewModel.MAX_STEP = 3

        viewModel.current_step  = 1
        viewModel.previous_step = 0
        viewModel.addresss = ""

        viewModel.requestLot = Lot()
        viewModel.rate1= Rate()
        viewModel.rate2 = Rate()
        viewModel.rate3= Rate()
        viewModel.rate4= Rate()
        viewModel.rate5 = Rate()

        viewModel.imageOneUri  = null
        viewModel.imageTwoUri  = null
        viewModel.imageThreeUri  = null

        viewModel.imageOneLabel = ""
        viewModel.imageTwoLabel = ""
        viewModel.imageThreeLabel = ""

        job.cancel()
        super.onDestroy()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job



    private fun initToolbar() {

        toolbar2.setNavigationIcon(R.drawable.ic_back_arrow)
        setSupportActionBar(toolbar2)

        supportActionBar!!.title = "Parking lot"
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
                        viewModel.requestLot.name = input_parking_lot_name.text.toString()
                        viewModel.requestLot.streetName = input_street_name.text.toString()
                        viewModel.requestLot.parkingSpaces = input_picker_parking_spaces.value
                        viewModel.requestLot.email = input_email.text.toString()
                        viewModel.requestLot.contactNumber =  input_parking_lot_contact_no.text.toString()
                        viewModel.requestLot.paybillNumber = input_paybill_no.text.toString()


                       navigateNextStepper(progressActive)
                    }
                } else if(progressActive ==2)
                {
                    if (validateSecondSetInputs()) {


                        //save first rate to viewmodel
                        viewModel.rate1.minimumTime = Integer.parseInt(input_min_time.text.toString())
                        viewModel.rate1.maximumTime =  Integer.parseInt(input_max_time.text.toString())
                        viewModel.rate1.cost =   input_cost.text.toString().toDouble()

                        //save second rate to viewmodel
                        if (
                                input_max_time_two.text.toString().trim { it <= ' ' }.isNotEmpty() ||
                                input_cost_two.text.toString().trim { it <= ' ' }.isNotEmpty()
                        ) run {
                            viewModel.rate2.minimumTime = Integer.parseInt(input_min_time_two.text.toString())
                            viewModel.rate2.maximumTime = Integer.parseInt(input_max_time_two.text.toString() )
                            viewModel.rate2.cost =   input_cost_two.text.toString().toDouble()

                        }

                        //save third rate to viewmodel
                        if (
                                input_max_time_three.text.toString().trim { it <= ' ' }.isNotEmpty() ||
                                input_cost_three.text.toString().trim { it <= ' ' }.isNotEmpty()
                        ) run {
                            viewModel.rate3.minimumTime = Integer.parseInt(input_min_time_three.text.toString())
                            viewModel.rate3.maximumTime = Integer.parseInt(input_max_time_three.text.toString())
                            viewModel.rate3.cost =   input_cost_three.text.toString().toDouble()

                        }
                        //save fourth rate to viewmodel
                        if (
                                input_max_time_four.text.toString().trim { it <= ' ' }.isNotEmpty() ||
                                input_cost_four.text.toString().trim { it <= ' ' }.isNotEmpty()
                        ) run {
                            viewModel.rate4.minimumTime = Integer.parseInt(input_min_time_four.text.toString() )
                            viewModel.rate4.maximumTime = Integer.parseInt(input_max_time_four.text.toString())
                            viewModel.rate4.cost =   input_cost_four.text.toString().toDouble()

                        }

                        //save fifth rate to viewmodel
                        viewModel.rate5.minimumTime = Integer.parseInt(input_min_time_five.text.toString())
                        viewModel.rate5.maximumTime =  10000
                        viewModel.rate5.cost =   input_cost_five.text.toString().toDouble()


                        navigateNextStepper(progressActive)
                    }
                }

        }else{
            if (validateImageSelection()) {

                lyt_progress.visibility = View.VISIBLE
                bottom_nav_layt.visibility = View.GONE

                val image1: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, viewModel.imageOneUri)
                val image2: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, viewModel.imageTwoUri)
                val image3: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, viewModel.imageThreeUri)

                val byteArrayOutputStream1 = ByteArrayOutputStream()
                image1.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream1)
                val byteArray1 = byteArrayOutputStream1.toByteArray()
                val encodedImage1: String = Base64.encodeToString(byteArray1, Base64.DEFAULT)

                val byteArrayOutputStream2 = ByteArrayOutputStream()
                image2.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream2)
                val byteArray2 = byteArrayOutputStream2.toByteArray()
                val encodedImage2: String = Base64.encodeToString(byteArray2, Base64.DEFAULT)

                val byteArrayOutputStream3 = ByteArrayOutputStream()
                image3.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream3)
                val byteArray3 = byteArrayOutputStream3.toByteArray()
                val encodedImage3: String = Base64.encodeToString(byteArray3, Base64.DEFAULT)

                val lotImage1 = LotImage(encodedImage1, viewModel.imageOneLabel)
                val lotImage2 = LotImage(encodedImage2, viewModel.imageTwoLabel)
                val lotImage3 = LotImage(encodedImage3, viewModel.imageThreeLabel)
                val lotImages: ArrayList<LotImage> = arrayListOf(lotImage1, lotImage2, lotImage3)
                viewModel.requestLot.parkingLotPhotos = lotImages

                val ratesList = ArrayList<Rate>()
                ratesList.add(viewModel.rate1)
                if (viewModel.rate2.minimumTime != 0) {
                    ratesList.add(viewModel.rate2)
                }
                if (viewModel.rate3.minimumTime != 0) {
                    ratesList.add(viewModel.rate3)
                }
                if (viewModel.rate4.minimumTime != 0) {
                    ratesList.add(viewModel.rate4)
                }
                ratesList.add(viewModel.rate5)

                viewModel.requestLot.parkingRates = ratesList

                Log.d("Parking Lot", viewModel.requestLot.toString())



                launch {
                    withContext(threadPool){
                        retrofitApiService.registerParkingLot(viewModel.requestLot)
                                .observe(context as ParkingLotRegistrationActivity, Observer<ApiResponse<LotResponse>> { response ->
                        if (response != null) {
                            // Log.d("Reg Resp", response.toString())
                            // Log.d("Reg Resp error", response.errorMessage.toString())
                            if (response.errorMessage != null) {
                                bottom_nav_layt.visibility = View.VISIBLE
                                display_txt_register_status.text = "Failed! Try Again."
                                display_txt_register_status.setTextColor(resources.getColor(R.color.red_600))
                                display_txt_register_status.visibility = View.VISIBLE
                                lyt_progress.visibility = View.GONE
                                btn_close_registration.visibility = View.GONE
                            } else {

                                bottom_nav_layt.visibility = View.GONE
                                display_txt_register_status.text = "Parking Lot Registration Successful."
                                display_txt_register_status.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                                display_txt_register_status.visibility = View.VISIBLE
                                lyt_progress.visibility = View.GONE
                                btn_close_registration.visibility = View.VISIBLE
                                btn_close_registration.setOnClickListener {
                                    finish()
                                }

                            }
                        } else {
                            bottom_nav_layt.visibility = View.VISIBLE
                            display_txt_register_status.text = "Failed! Try Again."
                            display_txt_register_status.setTextColor(resources.getColor(R.color.red_600))
                            display_txt_register_status.visibility = View.VISIBLE
                            lyt_progress.visibility = View.GONE
                            btn_close_registration.visibility = View.GONE
                        }
                    })
                        Log.d("In Coroutines","Kotlin future")
                    }
                }

            }
        }


    }

    private fun navigateNextStepper(progress:Int){
        var current  = progress
        current++
        viewModel.current_step = current
        if (viewModel.current_step>1){
            lyt_back.visibility = View.VISIBLE
        }else{
            lyt_back.visibility = View.INVISIBLE
        }
        if (viewModel.current_step == viewModel.MAX_STEP){
            lyt_next_txt_view.text = "FINISH"
        }

        progressStepper(viewModel.current_step)
    }

    private fun progressStepper(current:Int)
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
            2-> LotRegistrationStepTwoFragment::class.java
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

    private fun validateFirstSetInputs():Boolean {


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
        if (viewModel.requestLot.longitude == 0.0 && viewModel.requestLot.latitude == 0.0) run {
            input_layout_location.error = "Select Location!"
            UtilityClass.requestFocus(input_layout_street_name, window)
          return false
        }
        if (input_picker_parking_spaces.value == 1) run {
            input_layout_spaces.error = "Set spaces available!!"
            UtilityClass.requestFocus(input_layout_spaces, window)
            return false
        }
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


        return true
    }

    private fun validateSecondSetInputs():Boolean {


                    //validate first row of parking rates

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
                         input_layout_cost.error = "Enter cost!"
                         UtilityClass.requestFocus(input_layout_cost,window)
                         return false
                     }


                    //validate second row of parking rates

                    if (
                            input_max_time_two.text.toString().trim { it <= ' ' }.isNotEmpty() ||
                            input_cost_two.text.toString().trim { it <= ' ' }.isNotEmpty()
                    ) run {

                        if (input_min_time_two.text.toString().trim { it <= ' ' }.isEmpty()) {
                            input_layout_cost_two.error = "Enter min time!"
                            UtilityClass.requestFocus(input_layout_min_time_two,window)
                            return false
                        }

                        if (input_max_time_two.text.toString().trim { it <= ' ' }.isEmpty()) {
                            input_layout_max_time_two.error = "Enter max time!"
                            UtilityClass.requestFocus(input_layout_max_time_two,window)
                            return false
                        }

                        if (input_cost_two.text.toString().trim { it <= ' ' }.isEmpty()) {
                            input_layout_cost_two.error = "Enter duration cost!"
                            UtilityClass.requestFocus(input_layout_cost_two,window)
                            return false
                        }
                    }
             //validate third row of parking rates

                if (
                        input_max_time_three.text.toString().trim { it <= ' ' }.isNotEmpty() ||
                        input_cost_three.text.toString().trim { it <= ' ' }.isNotEmpty()
                ) run {

                    if (input_min_time_three.text.toString().trim { it <= ' ' }.isEmpty()) {
                        input_layout_min_time_three.error = "Enter min time!"
                        UtilityClass.requestFocus(input_layout_min_time_three,window)
                        return false
                    }

                    if (input_max_time_three.text.toString().trim { it <= ' ' }.isEmpty()) {
                        input_layout_max_time_three.error = "Enter max time!"
                        UtilityClass.requestFocus(input_layout_max_time_three,window)
                        return false
                    }

                    if (input_cost_three.text.toString().trim { it <= ' ' }.isEmpty()) {
                        input_layout_cost_three.error = "Enter duration cost!"
                        UtilityClass.requestFocus(input_layout_cost_three,window)
                        return false
                    }
                }

                //validate fourth row of parking rates

                if (
                        input_max_time_four.text.toString().trim { it <= ' ' }.isNotEmpty() ||
                        input_cost_four.text.toString().trim { it <= ' ' }.isNotEmpty()
                ) run {

                    if (input_min_time_four.text.toString().trim { it <= ' ' }.isEmpty()) {
                        input_layout_min_time_four.error = "Enter min time!"
                        UtilityClass.requestFocus(input_layout_min_time_four,window)
                        return false
                    }

                    if (input_max_time_four.text.toString().trim { it <= ' ' }.isEmpty()) {
                        input_layout_max_time_four.error = "Enter max time!"
                        UtilityClass.requestFocus(input_layout_max_time_four,window)
                        return false
                    }

                    if (input_cost_four.text.toString().trim { it <= ' ' }.isEmpty()) {
                        input_layout_cost_four.error = "Enter duration cost!"
                        UtilityClass.requestFocus(input_layout_cost_four,window)
                        return false
                    }
                }
                //validate fifth row of parking rates

                if (input_min_time_five.text.toString().trim { it <= ' ' }.isEmpty()) run {
                    input_layout_min_time_five.error = "Enter min overtime!"
                    UtilityClass.requestFocus(input_layout_cost,window)
                    return false
                }

                if (input_cost_five.text.toString().trim { it <= ' ' }.isEmpty()) run {
                    input_layout_cost_five.error = "Enter min overtime charge!"
                    UtilityClass.requestFocus(input_layout_cost,window)
                    return false
                }

                    return true
                }

    private fun validateImageSelection():Boolean{
        if (viewModel.imageOneUri==null  || viewModel.imageTwoUri==null || viewModel.imageThreeUri==null){
            display_txt_register_status.text = "Please add three photos of your parking lot."
            display_txt_register_status.setTextColor(resources.getColor(R.color.red_600))
            display_txt_register_status.visibility = View.VISIBLE

            return false


        }else{
            display_txt_register_status.text = "Registration success!"
            display_txt_register_status.setTextColor(resources.getColor(R.color.colorPrimaryDark))
            display_txt_register_status.visibility = View.GONE
        }
        return  true

    }

    private fun changeHeader(pos:Int){
        when(pos){
            1->txtRegHeader.text = "Registration"
            2->txtRegHeader.text = "Parking Charges"
            3->txtRegHeader.text = "Photos"
        }
    }


    }
