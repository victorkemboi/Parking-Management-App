package com.park254.app.park254.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.park254.app.park254.R
import kotlinx.android.synthetic.main.activity_lot_info.*
import java.text.SimpleDateFormat
import java.util.*

class LotInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lot_info)

        initToolbar()

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
            }else if(dateChecker == 2){
                input_pick_end_date.setText(sdf.format(date.time))
            }

        }

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            time.set(Calendar.HOUR_OF_DAY, hour)
            time.set(Calendar.MINUTE, minute)
            val myFormat = "HH:mm"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            if(timeChecker == 1){
                input_start_time.setText(sdf.format(time.time))
            }else if (timeChecker==2){
                input_end_time.setText(sdf.format(time.time))
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

        fab_book.setOnClickListener{
            if(validateBookingForm()){

            }

        }
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
        return true
    }
}
