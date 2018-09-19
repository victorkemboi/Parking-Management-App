package com.park254.app.park254.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.models.Booking
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.ui.adapters.BookingsListAdapter
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import kotlinx.android.synthetic.main.activity_bookings.*
import kotlinx.android.synthetic.main.activity_lot_info.*
import java.util.ArrayList
import javax.inject.Inject

class BookingsActivity : AppCompatActivity() {

    @Inject
    lateinit var retrofitApiService: RetrofitApiService
    private var mAdapter: BookingsListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookings)
        initToolbar()
        (application as App).applicationInjector.inject(this)

        bookings_recyclerview.layoutManager = LinearLayoutManager(this)

        bookings_recyclerview.setHasFixedSize(false)
        retrofitApiService.getUserBookings().observe(this, Observer<ApiResponse<List<Booking>>> {
            response->run{
            if (response?.body != null && response.isSuccessful) {

                mAdapter = BookingsListAdapter(this, response.body as ArrayList<Booking>)

                bookings_recyclerview.adapter = mAdapter
            }

        }
        })


    }
    private fun initToolbar() {
        toolbar2.setNavigationIcon(R.drawable.ic_back_arrow)
        setSupportActionBar(toolbar2)
        supportActionBar!!.title = "My Bookings"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar2.setNavigationOnClickListener{
            finish()
        }


    }
}
