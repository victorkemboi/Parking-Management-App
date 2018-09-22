package com.park254.app.park254.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.models.Payment
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.ui.adapters.BookingsListAdapter
import com.park254.app.park254.ui.adapters.PaymentsListAdapter
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import kotlinx.android.synthetic.main.activity_lot_info.*
import kotlinx.android.synthetic.main.activity_payments.*
import java.util.ArrayList
import javax.inject.Inject

class PaymentsActivity : AppCompatActivity() {

    @Inject
    lateinit var retrofitApiService: RetrofitApiService
    private var mAdapter: PaymentsListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payments)

        (application as App).applicationInjector.inject(this)
        initToolbar()


        payments_recyclerview.layoutManager = LinearLayoutManager(this)

        payments_recyclerview.setHasFixedSize(false)
        retrofitApiService.getUserPayments().observe(this, Observer<ApiResponse<List<Payment>>> {
            response->run{
            if (response?.body != null && response.isSuccessful) {

                if(response.body.isNotEmpty()) {
                    mAdapter = PaymentsListAdapter(this, response.body as ArrayList<Payment>)
                    mAdapter!!.onItemClick = {

                    }

                    payments_recyclerview.adapter = mAdapter

                    payments_preview.visibility = View.GONE
                    payments_recyclerview.visibility = View.VISIBLE

                }
            }

        }
        })
    }

    private fun initToolbar() {
        toolbar2.setNavigationIcon(R.drawable.ic_back_arrow)
        setSupportActionBar(toolbar2)
        supportActionBar!!.title = "My Payments"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar2.setNavigationOnClickListener{
            finish()
        }


    }
}
