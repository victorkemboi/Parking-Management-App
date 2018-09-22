package com.park254.app.park254.ui

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.models.Employee
import com.park254.app.park254.models.LotResponse
import com.park254.app.park254.models.User
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.ui.adapters.EmployeeListAdapter
import com.park254.app.park254.ui.repo.HomeViewModel
import com.park254.app.park254.ui.repo.ParkingLotRegistrationViewModel
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import kotlinx.android.synthetic.main.activity_employees.*
import kotlinx.android.synthetic.main.fragment_main_home.*
import java.util.ArrayList
import javax.inject.Inject

class EmployeeActivity : AppCompatActivity() {
    @Inject
    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var viewModel: ParkingLotRegistrationViewModel

    @Inject
    lateinit var  retrofitApiService: RetrofitApiService

    private var employeesAdapter: EmployeeListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employees)
        (application as App).applicationInjector.inject(this)

        viewModel.lotResponse = homeViewModel.parsedLot!!

        initToolbar()

        btn_add_employee.setOnClickListener{
            startActivity(Intent(this@EmployeeActivity, RegisterEmployeeActivity::class.java))
        }

        employees_recycler_view.layoutManager = LinearLayoutManager(this)
        employees_recycler_view.setHasFixedSize(false)

        retrofitApiService.getParkingLotEmployees(viewModel.lotResponse.id).observe(this, Observer <ApiResponse<ArrayList<User>>> {
            response ->run {
            if (response!=null) {
                if (response.isSuccessful) {
                    if (response.body != null) {

                        if (response.body.isNotEmpty()){


                            owner_preview.visibility = View.GONE

                            owner_parking_lots.visibility = View.VISIBLE

                            employeesAdapter = EmployeeListAdapter(this, response.body)

                            employees_recycler_view.adapter = employeesAdapter

                        }
                    }
                }
            }
        }
        })

    }


     fun initToolbar() {
         toolbar_emp.setNavigationIcon(R.drawable.ic_back_arrow)
        setSupportActionBar(toolbar_emp)
        supportActionBar!!.title =  "Employees"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
         toolbar_emp.setNavigationOnClickListener{
            finish()
        }


    }


}
