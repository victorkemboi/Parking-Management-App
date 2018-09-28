package com.park254.app.park254.ui

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.widget.SwipeRefreshLayout
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
import android.support.v7.app.AlertDialog
import android.util.Log
import com.google.gson.Gson
import com.park254.app.park254.utils.UtilityClass
import kotlinx.android.synthetic.main.activity_bookings.*
import kotlinx.coroutines.experimental.*
import kotlin.coroutines.experimental.CoroutineContext


class PaymentsActivity : AppCompatActivity(),  CoroutineScope, SwipeRefreshLayout.OnRefreshListener  {



    @Inject
    lateinit var retrofitApiService: RetrofitApiService
    @Inject
    lateinit var job: Job

    @Inject
    lateinit var threadPool : ExecutorCoroutineDispatcher

    private var mAdapter: PaymentsListAdapter? = null

    private val paymentActivityContext = this
    val activityClass = this as Activity

    var paymentStringOnRequestPermission = ""

    override val coroutineContext: CoroutineContext
        get() =   Dispatchers.Default + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payments)

        (application as App).applicationInjector.inject(this)
        initToolbar()

        payments_swipe_container.setOnRefreshListener(this);
        payments_swipe_container.setColorSchemeColors(
                resources.getColor( android.R.color.holo_green_dark),
                resources.getColor(android.R.color.holo_red_dark)  ,
                resources.getColor(android.R.color.holo_blue_dark)   ,
                resources.getColor(android.R.color.holo_orange_dark)   )

        setPayments()



    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            UtilityClass.saveImage(UtilityClass.encodeDataToQR(paymentStringOnRequestPermission,paymentActivityContext)!!,paymentActivityContext,activityClass)

        }
    }

    override fun onRefresh() {

        setPayments()
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

    private fun setPayments(){
        if (!payments_swipe_container.isRefreshing){
            payments_swipe_container.isRefreshing = true
        }
        payments_recyclerview.layoutManager = LinearLayoutManager(this)
        payments_recyclerview.setHasFixedSize(false)
        launch {
            withContext(threadPool) {
                retrofitApiService.getUserPayments().observe(this@PaymentsActivity, Observer<ApiResponse<List<Payment>>> {
                    response->run{
                    if (response?.body != null && response.isSuccessful) {

                        if(response.body.isNotEmpty()) {
                            mAdapter = PaymentsListAdapter(this@PaymentsActivity, response.body as ArrayList<Payment>)
                            mAdapter!!.onItemClick = {

                                payment->
                                generateQrCodeDialog(payment)
                            }

                            payments_recyclerview.adapter = mAdapter

                            payments_preview.visibility = View.GONE
                            payments_recyclerview.visibility = View.VISIBLE

                        }
                    }

                }
                    this@PaymentsActivity.run {
                        if (payments_swipe_container.isRefreshing){
                            payments_swipe_container.isRefreshing = false
                        }
                    }

                })
            }
        }




    }

    private fun generateQrCodeDialog(payment: Payment) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Generate QR Code")

        val view = layoutInflater.inflate(R.layout.create_qrcode_pop_out, null)


        builder.setView(view);

        builder.setPositiveButton("OK") { dialog, p1 ->


            launch {
                withContext(threadPool) {
                    val paymentQRString = Gson().toJson(payment)

                    //Log.d("create qr","start")


                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED) {
                            UtilityClass.saveImage(UtilityClass.encodeDataToQR(paymentQRString,paymentActivityContext)!!,paymentActivityContext,activityClass)

                        } else {
                            paymentStringOnRequestPermission = paymentQRString

                            ActivityCompat.requestPermissions(paymentActivityContext, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 101)

                        }
                    }
                    else { //permission is automatically granted on sdk<23 upon installation
                        UtilityClass.saveImage(UtilityClass.encodeDataToQR(paymentQRString,paymentActivityContext)!!,paymentActivityContext,activityClass)

                    }
                    //Log.d("create qr","finished")
                }
            }

        }



        builder.setNegativeButton(android.R.string.cancel) { dialog, p1 ->
            dialog.cancel()
        }

        builder.show()
    }


}
