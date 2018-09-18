package com.park254.app.park254.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.ui.repo.HomeViewModel
import kotlinx.android.synthetic.main.activity_owner_lot_info.*
import javax.inject.Inject

class OwnerLotInfoActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_lot_info)
        (application as App).applicationInjector.inject(this)

        if(viewModel.parsedLot !=null){
            owner_lot_info_name.text = viewModel.parsedLot!!.name
            owner_lot_info_street.text = viewModel.parsedLot!!.streetName
        }

        fab_update_info.setOnClickListener{
            startActivity(Intent(this@OwnerLotInfoActivity, UpdateInfoActivity::class.java))
        }

        fab_employees.setOnClickListener{
            startActivity(Intent(this@OwnerLotInfoActivity, EmployeeActivity::class.java))
        }

    }
}
