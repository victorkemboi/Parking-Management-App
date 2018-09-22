package com.park254.app.park254.ui

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.models.Booking
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.ui.adapters.BookingsListAdapter
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import kotlinx.android.synthetic.main.activity_bookings.*
import kotlinx.android.synthetic.main.activity_lot_info.*
import kotlinx.android.synthetic.main.payment_pop_out_lyt.view.*
import kotlinx.coroutines.experimental.*
import java.util.ArrayList
import javax.inject.Inject
import kotlin.coroutines.experimental.CoroutineContext

class BookingsActivity : AppCompatActivity() ,View.OnClickListener, CoroutineScope {
    override fun onClick(p0: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Inject
    lateinit var retrofitApiService: RetrofitApiService

    @Inject
    lateinit var job: Job

    @Inject
    lateinit var threadPool : ExecutorCoroutineDispatcher

    private var mAdapter: BookingsListAdapter? = null

    private val bookingsActivityContext = this

    override val coroutineContext: CoroutineContext
        get() =   Dispatchers.Default + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookings)

        (application as App).applicationInjector.inject(this)
        initToolbar()
        bookings_recyclerview.layoutManager = LinearLayoutManager(this)

        bookings_recyclerview.setHasFixedSize(false)
        retrofitApiService.getUserBookings().observe(this, Observer<ApiResponse<List<Booking>>> {
            response->run{
            if (response?.body != null && response.isSuccessful) {

                if(response.body.isNotEmpty()) {
                    mAdapter = BookingsListAdapter(this, response.body as ArrayList<Booking>)
                    mAdapter!!.onItemClick = {
                        booking ->

                        paymentDialog(booking)
                    }

                    bookings_recyclerview.adapter = mAdapter

                    bookings_preview.visibility = View.GONE
                    bookings_recyclerview.visibility = View.VISIBLE

                }
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
    private fun paymentDialog(booking:Booking) {
        val builder = AlertDialog.Builder(bookingsActivityContext)
        builder.setTitle("Booking Payment")

        val view = layoutInflater.inflate(R.layout.payment_pop_out_lyt, null)


        builder.setView(view);

        builder.setPositiveButton("PAY") { dialog, p1 ->


        launch {
                withContext(threadPool) {

                    retrofitApiService.payForBooking(booking.id).observe(
                            bookingsActivityContext, Observer<ApiResponse<Void>> {
                        Log.d("Resp","MPESA")
                    }
                    )
                }
            }

        }



        builder.setNegativeButton(android.R.string.cancel) { dialog, p1 ->
            dialog.cancel()
        }

        builder.show()
    }


}
