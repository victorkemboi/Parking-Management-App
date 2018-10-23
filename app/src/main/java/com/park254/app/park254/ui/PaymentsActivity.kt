package com.park254.app.park254.ui

import android.arch.lifecycle.Observer
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.models.Payment
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.ui.adapters.PaymentsListAdapter
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import kotlinx.android.synthetic.main.activity_lot_info.*
import kotlinx.android.synthetic.main.activity_payments.*
import java.util.ArrayList
import javax.inject.Inject
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.park254.app.park254.ui.repo.PaymentsViewModel
import com.park254.app.park254.utils.UtilityClass
import dagger.android.AndroidInjection
import kotlinx.coroutines.experimental.*
import kotlin.coroutines.experimental.CoroutineContext


class PaymentsActivity : AppCompatActivity(),  CoroutineScope, SwipeRefreshLayout.OnRefreshListener  {



    @Inject
    lateinit var retrofitApiService: RetrofitApiService
    @Inject
    lateinit var job: Job

    @Inject
    lateinit var viewModel: PaymentsViewModel

    @Inject
    lateinit var threadPool : ExecutorCoroutineDispatcher

    private var mAdapter: PaymentsListAdapter? = null

    private val paymentActivityContext = this

    var paymentStringOnRequestPermission = ""

    override val coroutineContext: CoroutineContext
        get() =   Dispatchers.Default + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payments)

        AndroidInjection.inject(this)
        initToolbar()

        payments_swipe_container.setOnRefreshListener(this)
        payments_swipe_container.setColorSchemeColors(
                ContextCompat.getColor( paymentActivityContext,android.R.color.holo_green_dark),
                ContextCompat.getColor(paymentActivityContext,android.R.color.holo_red_dark)  ,
                ContextCompat.getColor(paymentActivityContext, android.R.color.holo_blue_dark)   ,
                ContextCompat.getColor(paymentActivityContext, android.R.color.holo_orange_dark)   )

        payments_recyclerview.layoutManager = LinearLayoutManager(paymentActivityContext)
        payments_recyclerview.setHasFixedSize(false)

        setPayments()



    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            val qrBitMap =UtilityClass.encodeDataToQR(paymentStringOnRequestPermission,paymentActivityContext)!!

            val qrBuilder = AlertDialog.Builder(paymentActivityContext)
            qrBuilder.setTitle("QR Code")

            val qrView = layoutInflater.inflate(R.layout.display_qr_code_layout, null)
            val qrImgView =  qrView.findViewById<ImageView>(R.id.qr_code_image)

            Glide.with(paymentActivityContext).load(qrBitMap).into(qrImgView)

            qrBuilder.setView(qrView)

            qrBuilder.setPositiveButton("Exit") { dialog, p1 ->
                dialog.dismiss()
            }

            qrBuilder.show()
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


        launch {
            withContext(threadPool) {
                retrofitApiService.getUserPayments().observe(this@PaymentsActivity, Observer<ApiResponse<List<Payment>>> {
                    response->run{
                    if (response?.body != null && response.isSuccessful) {

                        if(response.body.isNotEmpty()) {
                            mAdapter = PaymentsListAdapter(this@PaymentsActivity, response.body as ArrayList<Payment>)
                            mAdapter!!.onItemClick = {

                                payment->

                                lyt_progress_qr_code.visibility = View.VISIBLE

                                Handler().postDelayed({

                                    generateQrCodeDialog(payment)
                                }, 1000)


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

        val paymentBitmap: Bitmap?
        val tempQRCode = viewModel.qrCodesHashMap[payment]
        if (tempQRCode == null){
            Log.w("Dialog: ","start process photo")
            val paymentQRString = Gson().toJson(payment)
            paymentBitmap = UtilityClass.encodeDataToQR(paymentQRString,paymentActivityContext)!!
            viewModel.qrCodesHashMap[payment] = paymentBitmap
        }else{
            paymentBitmap = viewModel.qrCodesHashMap[payment]
        }


            val builder = AlertDialog.Builder(paymentActivityContext)
            builder.setTitle("Payment Verification")

            val view = layoutInflater.inflate(R.layout.qr_code_dialog_layout, null)

            val qrImgView = view.findViewById<ImageView>(R.id.qr_code_image)

            qrImgView.setImageBitmap(paymentBitmap)

            builder.setNeutralButton("Close"){
                dialog,i ->
                dialog.dismiss()

                lyt_progress_qr_code.visibility = View.GONE
            }

            builder.setView(view)

            builder.show()

        }

    }


