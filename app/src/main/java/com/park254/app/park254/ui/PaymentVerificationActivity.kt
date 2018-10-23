package com.park254.app.park254.ui

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.ui.fragments.PaymentVerificationSuccessFragment
import com.park254.app.park254.ui.repo.PaymentVerificationViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_payment_verification.*
import kotlinx.android.synthetic.main.payment_verification_dialog.*
import kotlinx.android.synthetic.main.toolbar_2.*
import javax.inject.Inject


class PaymentVerificationActivity : AppCompatActivity() {


    @Inject
    lateinit var paymentVerificationViewModel: PaymentVerificationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_verification)
        AndroidInjection.inject(this)
        initToolbar()
        submitAction()

    }

    private fun initToolbar() {

        toolbar2.setNavigationIcon(R.drawable.ic_back_arrow)
        setSupportActionBar(toolbar2)
        supportActionBar!!.title = "Payment Details"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar2.setNavigationOnClickListener {
            finish()
        }
    }



    private fun showDialogPaymentSuccess() {
        val fragmentManager = supportFragmentManager
        val newFragment = PaymentVerificationSuccessFragment()
        val transaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit()
    }

    private fun submitAction() {
        lyt_progress_attendant_qr_reader.visibility = View.VISIBLE

        Handler().postDelayed({
            showDialogPaymentSuccess()
            lyt_progress_attendant_qr_reader.visibility = View.GONE

        }, 1000)
    }




}
