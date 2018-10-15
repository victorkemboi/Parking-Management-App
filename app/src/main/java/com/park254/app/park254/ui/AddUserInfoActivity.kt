package com.park254.app.park254.ui

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.models.User
import com.park254.app.park254.models.UserUpdate
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.ui.repo.LoginViewModel
import com.park254.app.park254.utils.SharedPrefs
import com.park254.app.park254.utils.UtilityClass.hideKeyboard
import com.park254.app.park254.utils.UtilityClass.requestFocus
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import kotlinx.android.synthetic.main.activity_add_user_info.*
import javax.inject.Inject

class AddUserInfoActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    @Inject
    lateinit var viewModel: LoginViewModel

    @Inject
    lateinit var retrofitApiService: RetrofitApiService
    @Inject
    lateinit var settings: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user_info)
        (application as App).applicationInjector.inject(this)

        btn_finish_sign_in.setOnClickListener { finishSignIn() }

        sp_gender!!.onItemSelectedListener = this

        val items = arrayOf("Select Gender.", "Male", "Female")

        sp_gender.adapter = ArrayAdapter<String>(this, R.layout.spinner_item, items)


        //Log.d("Token in shared pref", settings.token)


    }

    override fun onNothingSelected(parent: AdapterView<*>?) {


        //  result.text = "please select an option"
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


        viewModel.gender = position
    }


    private fun finishSignIn() {
        if (validateInputIsNotNull()) {
            viewModel.phoneNumber = input__phone_number.text.toString()

            //viewModel.api  patch user

            hideKeyboard(this)
            btn_finish_sign_in.visibility = View.GONE
            lyt_progress_login.visibility = View.VISIBLE

            retrofitApiService.registerUser().observe(this, Observer<ApiResponse<User>> { response ->
                if (response?.body != null) {

                    settings.userId = response.body.id
                    val userUpdate = UserUpdate()

                    val gender = when (viewModel.gender) {
                        1 -> "Male"
                        2 -> "Female"
                        else -> ""
                    }
                    userUpdate.gender = gender
                    userUpdate.phoneNumber = viewModel.phoneNumber

                    retrofitApiService.updateUserInfo(userUpdate).observe(
                            this, Observer {
                        run {
                            startActivity(
                                    Intent(this@AddUserInfoActivity, HomeActivity::class.java))
                            finish()
                        }
                    }
                    )


                }


            })


        }


    }

    private fun validateInputIsNotNull(): Boolean {
        if (input__phone_number.text.toString().trim { it <= ' ' }.isEmpty()) run {
            input_layout_phone_number.error = "Enter Phone Number!"
            requestFocus(input__phone_number, window)
            return false
        }
        else if (viewModel.gender == 0) {
            input_layout_gender.error = "Select Gender!"
            requestFocus(sp_gender, window)
            return false
        }

        return true
    }


    override fun onBackPressed() {

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser = firebaseAuth.currentUser!!

        currentUser.delete().addOnCompleteListener { task ->
            run {
                if (task.isSuccessful) {
                    startActivity(
                            Intent(this@AddUserInfoActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }

    }
}
