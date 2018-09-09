package com.park254.app.park254.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.ui.repo.LoginViewModel
import com.park254.app.park254.utils.UtilityClass.hideKeyboard
import com.park254.app.park254.utils.UtilityClass.requestFocus
import kotlinx.android.synthetic.main.activity_add_user_info.*
import kotlinx.android.synthetic.main.activity_add_user_info.view.*
import javax.inject.Inject

class AddUserInfoActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {
    @Inject
    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user_info)
        App.applicationInjector.inject(this)

        btn_finish_sign_in.setOnClickListener { finishSignIn() }

        sp_gender!!.onItemSelectedListener = this

       val items  = arrayOf("Select Gender.","Male", "Female")

        sp_gender.adapter = ArrayAdapter<String>(this,R.layout.spinner_item,items)


    }

    override fun onNothingSelected(parent: AdapterView<*>?) {


        //  result.text = "please select an option"
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


        viewModel.gender = position
    }



    fun finishSignIn(){
        if (validateInputIsNotNull()){
            viewModel.phoneNumber = input__phone_number.text.toString()

            //viewModel.api  patch user

            hideKeyboard(this)

            startActivity(
                    Intent(this@AddUserInfoActivity, HomeActivity::class.java))
        }


    }

    fun validateInputIsNotNull(): Boolean{
        if (input__phone_number.text.toString().trim { it <= ' ' }.isEmpty()) run {
            input_layout_phone_number.error = "Enter Phone Number!"
            requestFocus(input__phone_number,window)
            return false
        }
        else if (viewModel.gender == 0){
            input_layout_gender.error = "Select Gender!"
            requestFocus(sp_gender,window)
            return false
        }

        return true
    }








}
