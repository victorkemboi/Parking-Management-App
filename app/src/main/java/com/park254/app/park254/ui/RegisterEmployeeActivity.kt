package com.park254.app.park254.ui

import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.models.Employee
import com.park254.app.park254.models.User
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.ui.repo.EmployeeViewModel
import com.park254.app.park254.ui.repo.HomeViewModel
import com.park254.app.park254.utils.UtilityClass
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_register_employee.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RegisterEmployeeActivity : AppCompatActivity() {
    @Inject
    lateinit var retrofitApiService: RetrofitApiService
    @Inject
    lateinit var viewModel: EmployeeViewModel

    @Inject
    lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_employee)
        AndroidInjection.inject(this)

        btn_add_lot_employee.setOnClickListener { _ ->
            if(txt_input_employee_email.text.toString().trim { it <= ' ' }.isNotEmpty()) run {
                employeeRegistration()
            }

        }
    }

    private fun employeeRegistration(){
        btn_add_lot_employee.visibility = View.GONE
        lyt_progress_employee.visibility = View.VISIBLE
        retrofitApiService.getUserByEmail(txt_input_employee_email.text.toString()).observe(this, Observer<ApiResponse<User>> {
            response ->
            run {
                if (response != null) {
                    if (response.isSuccessful) {

                        if (response.body != null) {

                        viewModel.user = response.body

                        txt_status_add_employee.visibility = View.GONE
                        lyt_progress_employee.visibility = View.GONE
                        txt_input_employee_email.isClickable = false
                        txt_input_employee_start_date.visibility = View.VISIBLE
                        txt_input_employee_start_date.setOnClickListener {
                            val date = Calendar.getInstance()
                            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                                date.set(Calendar.YEAR, year)
                                date.set(Calendar.MONTH, monthOfYear)
                                date.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                                val myFormat = "dd/MM/yyyy" // mention the format you need
                                val sdf = SimpleDateFormat(myFormat, Locale.US)

                                txt_input_employee_start_date.setText(sdf.format(date.time))
                                viewModel.employee.commencingOn = UtilityClass.getStringTimeStampWithDate(date.time)
                            }

                            DatePickerDialog(this@RegisterEmployeeActivity, dateSetListener,
                                    date.get(Calendar.YEAR),
                                    date.get(Calendar.MONTH),
                                    date.get(Calendar.DAY_OF_MONTH)).show()
                        }


                        btn_add_lot_employee.text = "Add Employee"
                        btn_add_lot_employee.visibility = View.VISIBLE
                        btn_add_lot_employee.setOnClickListener { _ ->

                            btn_add_lot_employee.visibility = View.GONE
                            lyt_progress_employee.visibility = View.VISIBLE

                            if (txt_input_employee_start_date.text.toString().trim { it <= ' ' }.isNotEmpty()) run {


                                if (viewModel.user.id != "") {
                                    viewModel.employee.lotId = homeViewModel.parsedLot!!.id
                                    viewModel.employee.userId = viewModel.user.id


                                    retrofitApiService.registerEmployee(viewModel.employee).observe(
                                            this, Observer<ApiResponse<Employee>> { response ->
                                        run {

                                            if (response != null) {

                                                if (response.isSuccessful) {

                                                    txt_input_employee_start_date.isClickable = false
                                                    txt_status_add_employee.setBackgroundResource(R.color.colorAccent)
                                                    txt_status_add_employee.text = "Employee added successfully. "
                                                    txt_status_add_employee.visibility = View.VISIBLE

                                                    lyt_progress_employee.visibility = View.GONE
                                                    btn_add_lot_employee.visibility = View.VISIBLE
                                                    btn_add_lot_employee.text = "Finish"
                                                    btn_add_lot_employee.setOnClickListener { _ ->
                                                        finish()
                                                    }

                                                }
                                            }
                                        }

                                    })
                                }


                            }

                        }
                        lyt_progress_employee.visibility = View.GONE


                    }else{
                            txt_status_add_employee.setBackgroundResource(R.color.red_600)
                            txt_status_add_employee.text =  "Email does not exist! Ensure its the correct email! "
                            txt_status_add_employee.visibility = View.VISIBLE
                            lyt_progress_employee.visibility = View.GONE
                            btn_add_lot_employee.visibility = View.VISIBLE
                        }

                    }else{

                        txt_status_add_employee.setBackgroundResource(R.color.red_600)
                        txt_status_add_employee.text =  "Email does not exist! Ensure its the correct email! "
                        txt_status_add_employee.visibility = View.VISIBLE
                        lyt_progress_employee.visibility = View.GONE
                        btn_add_lot_employee.visibility = View.VISIBLE
                    }

                }else{

                    txt_status_add_employee.setBackgroundResource(R.color.red_600)
                    txt_status_add_employee.text =  "Email does not exist! Ensure its the correct email! "
                    txt_status_add_employee.visibility = View.VISIBLE
                    lyt_progress_employee.visibility = View.GONE
                    btn_add_lot_employee.visibility = View.VISIBLE
                }
            }
        })
    }
}
