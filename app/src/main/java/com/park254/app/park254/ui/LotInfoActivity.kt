package com.park254.app.park254.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.models.Booking
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.ui.adapters.ParkingLotAdapter
import com.park254.app.park254.ui.repo.HomeViewModel
import com.park254.app.park254.ui.repo.LotInfoViewModel
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import kotlinx.android.synthetic.main.activity_lot_info.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Build
import android.view.View
import com.park254.app.park254.models.EstimateRequest
import com.park254.app.park254.utils.UtilityClass

class LotInfoActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: HomeViewModel

    @Inject
    lateinit var lotInfoViewModel: LotInfoViewModel

    @Inject
    lateinit var  retrofitApiService: RetrofitApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lot_info)

        initToolbar()

        (application as App).applicationInjector.inject(this)

        if (viewModel.parsedLot !=null){
            lot_info_name.text = viewModel.parsedLot!!.name
            lot_info_street.text = viewModel.parsedLot!!.streetName

            val mAdapter: ParkingLotAdapter? = ParkingLotAdapter(viewModel.parsedLot!!.parkingRates, this)
            lot_rates_recycler_view.adapter = mAdapter

            //lot_info_parking_rate.text = "${viewModel.parsedLot.}"
        }

        val date = Calendar.getInstance()
        val time = Calendar.getInstance()

        var dateChecker = 0
        var timeChecker = 0

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            date.set(Calendar.YEAR, year)
            date.set(Calendar.MONTH, monthOfYear)
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            if(dateChecker == 1){
                input_pick_start_date.setText(sdf.format(date.time))
                lotInfoViewModel.checkInDate.set(Calendar.YEAR,year)
                lotInfoViewModel.checkInDate.set(Calendar.MONTH,monthOfYear)
                lotInfoViewModel.checkInDate.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            }else if(dateChecker == 2){
                input_pick_end_date.setText(sdf.format(date.time))
                lotInfoViewModel.checkOutDate.set(Calendar.YEAR,year)
                lotInfoViewModel.checkOutDate.set(Calendar.MONTH,monthOfYear)
                lotInfoViewModel.checkOutDate.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            }

        }

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            time.set(Calendar.HOUR_OF_DAY, hour)
            time.set(Calendar.MINUTE, minute)
            val myFormat = "HH:mm"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            if(timeChecker == 1){
                input_start_time.setText(sdf.format(time.time))
                lotInfoViewModel.checkInDate.set(Calendar.HOUR_OF_DAY,hour)
                lotInfoViewModel.checkInDate.set(Calendar.MINUTE,minute)
            }else if (timeChecker==2){
                input_end_time.setText(sdf.format(time.time))
                lotInfoViewModel.checkOutDate.set(Calendar.HOUR_OF_DAY,hour)
                lotInfoViewModel.checkOutDate.set(Calendar.MINUTE,minute)
            }

        }

        input_pick_start_date.setOnClickListener {
                dateChecker = 1
            DatePickerDialog(this@LotInfoActivity, dateSetListener,
                    date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DAY_OF_MONTH)).show()
        }

        input_pick_end_date.setOnClickListener {
            dateChecker = 2
            DatePickerDialog(this@LotInfoActivity, dateSetListener,
                    date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DAY_OF_MONTH)).show()
        }
        input_start_time.setOnClickListener {
            timeChecker = 1
            TimePickerDialog(this, timeSetListener, time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), true).show()
        }
        input_end_time.setOnClickListener {
            timeChecker = 2
            TimePickerDialog(this, timeSetListener, time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), true).show()
        }

        fab_book.setOnClickListener{ _ ->
            if(validateBookingForm()){


                if (viewModel.parsedLot != null){
                lotInfoViewModel.bookRequest.carRegistration = input_car_registrationg_no.text.toString()
                lotInfoViewModel.bookRequest.sarting = UtilityClass.getStringTimeStampWithDate( lotInfoViewModel.checkInDate.time)
                lotInfoViewModel.bookRequest.ending = UtilityClass.getStringTimeStampWithDate(lotInfoViewModel.checkOutDate.time)
                lotInfoViewModel.bookRequest.parkinglotId = viewModel.parsedLot!!.id

                    val estimateRequest = EstimateRequest(
                            lotInfoViewModel.bookRequest.parkinglotId,
                           lotInfoViewModel.bookRequest.sarting  ,
                           lotInfoViewModel.bookRequest.ending)


                    getBookingEstimate(estimateRequest)





                }
            }

        }
    }

    @SuppressLint("RestrictedApi")
    fun getBookingEstimate(estimateRequest: EstimateRequest){
        fab_book.visibility = View.INVISIBLE
        txt_book_tag.visibility =  View.INVISIBLE
        lyt_progress_book.visibility = View.VISIBLE
        retrofitApiService.getBookingEstimation(estimateRequest).observe(this, Observer<ApiResponse<Double>> {
            response -> run{
            if (response!=null){
                if (response.isSuccessful){
                    if (response.body != null){

                        display_txt_book_status.visibility = View.GONE

                        input_pick_start_date.isFocusableInTouchMode = false
                        input_pick_start_date.isFocusable = false
                        input_pick_start_date.isClickable = false

                        input_start_time.isFocusableInTouchMode = false
                        input_start_time.isFocusable = false
                        input_start_time.isClickable = false

                        input_pick_end_date.isFocusableInTouchMode = false
                        input_pick_end_date.isFocusable = false
                        input_pick_end_date.isClickable = false

                        input_end_time.isFocusableInTouchMode = false
                        input_end_time.isFocusable = false
                        input_end_time.isClickable = false
                        lyt_progress_book.visibility = View.GONE
                        input_car_registrationg_no.isFocusableInTouchMode = false
                        input_car_registrationg_no.isFocusable = false
                        input_car_registrationg_no.isClickable = false

                        btn_change_booking.setOnClickListener {
                            fab_book.visibility = View.VISIBLE
                            txt_book_tag.visibility =  View.VISIBLE
                            btn_change_booking.visibility = View.GONE
                            btn_confirm_booking.visibility = View.GONE

                            input_pick_start_date.isClickable = true

                            input_start_time.isClickable = true

                            input_pick_end_date.isClickable = true

                            input_end_time.isClickable = true

                            input_car_registrationg_no.isFocusableInTouchMode = true
                            input_car_registrationg_no.isFocusable = true
                            input_car_registrationg_no.isClickable = true
                        }

                        btn_change_booking.visibility = View.VISIBLE

                        btn_confirm_booking.setOnClickListener {

                            confirmBooking()
                        }

                        btn_confirm_booking.visibility = View.VISIBLE


                    }
                }
                else{
                    fab_book.visibility = View.VISIBLE
                    txt_book_tag.visibility =  View.VISIBLE
                    lyt_progress_book.visibility = View.GONE

                    if (Build.VERSION.SDK_INT < 23) {
                        display_txt_book_status.setTextColor(resources.getColor(R.color.red_600))
                    } else {
                        display_txt_book_status.setTextColor(applicationContext.getColor(R.color.red_600))
                    }

                    display_txt_book_status.text = "Booking estimation failed! Try again."
                    display_txt_book_status.visibility = View.VISIBLE
                }
            }
        }
        })
    }

    @SuppressLint("RestrictedApi")
    fun confirmBooking(){

        lyt_progress_book.visibility = View.VISIBLE
        btn_change_booking.visibility = View.GONE

        retrofitApiService.bookParkingLot(lotInfoViewModel.bookRequest).observe(this,Observer<ApiResponse<Booking>>{
            response -> run{
            if (response != null) {
                if (response.isSuccessful) {



                    if (response.body != null) {


                        startActivity(
                                Intent(this@LotInfoActivity, BookingsActivity::class.java))


                    } else {

                        if (Build.VERSION.SDK_INT < 23) {
                            display_txt_book_status.setTextColor(resources.getColor(R.color.red_600))
                        } else {
                            display_txt_book_status.setTextColor(applicationContext.getColor(R.color.red_600))
                        }
                        lyt_progress_book.visibility = View.GONE
                        btn_change_booking.visibility = View.VISIBLE

                        display_txt_book_status.text = "Booking process failed! Try again."
                        display_txt_book_status.visibility = View.VISIBLE
                    }



                }else{

                    if (Build.VERSION.SDK_INT < 23) {
                        display_txt_book_status.setTextColor(resources.getColor(R.color.red_600))
                    } else {
                        display_txt_book_status.setTextColor(applicationContext.getColor(R.color.red_600))
                    }
                    lyt_progress_book.visibility = View.VISIBLE
                    btn_change_booking.visibility = View.VISIBLE

                    display_txt_book_status.text = "Booking process failed! Try again."
                    display_txt_book_status.visibility = View.VISIBLE
                }
            }
        }

        })


    }


    private fun initToolbar() {
        toolbar2.setNavigationIcon(R.drawable.ic_back_arrow)
        setSupportActionBar(toolbar2)
        supportActionBar!!.title = "Parking Lot Info"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar2.setNavigationOnClickListener{
            finish()
        }


    }

    fun validateBookingForm(): Boolean{
        if ( input_pick_start_date.text.toString() == ""){
            input_layout_pick_start_date.error = "Please set check-in date!"
            return  false
        }else{
            input_layout_pick_start_date.error = null
        }

        if ( input_start_time.text.toString() == ""){
            input_layout_pick_start_time.error = "Please set check-in time!"
            return  false
        }else{
            input_layout_pick_start_time.error = null
        }

        if ( input_pick_end_date.text.toString() == ""){
            input_layout_pick_end_date.error = "Please set check-out date!"
            return  false
        }else{
            input_layout_pick_end_date.error = null
        }

        if ( input_end_time.text.toString() == ""){
            input_layout_pick_end_time.error = "Please set check out date!"
            return  false
        }else{
            input_layout_pick_end_time.error = null
        }
        if ( input_car_registrationg_no.text.toString() == ""){
            input_lyt_car_registrationg_no.error = "Please enter your car registration number!"
            return  false
        }else{
            input_lyt_car_registrationg_no.error = null
        }
        return true
    }
}
