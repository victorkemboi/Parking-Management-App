package com.park254.app.park254.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.ui.repo.HomeViewModel
import com.park254.app.park254.ui.repo.ParkingLotRegistrationViewModel
import kotlinx.android.synthetic.main.activity_employee.*
import kotlinx.android.synthetic.main.activity_lot_info.*
import javax.inject.Inject

class EmployeeActivity : AppCompatActivity() {
    @Inject
    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var viewModel: ParkingLotRegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee)
        (application as App).applicationInjector.inject(this)

        viewModel.lot = homeViewModel.parsedLot!!
        initToolbar()
        btn_add_employee.setOnClickListener{
            startActivity(Intent(this@EmployeeActivity, RegisterEmployeeActivity::class.java))
        }



    }


     fun initToolbar() {
         toolbar_emp.setNavigationIcon(R.drawable.ic_back_arrow)
        setSupportActionBar(toolbar_emp)
        supportActionBar!!.title = "Parking Lot Info"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
         toolbar_emp.setNavigationOnClickListener{
            finish()
        }


    }


}
