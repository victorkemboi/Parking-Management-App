package com.park254.app.park254.ui.repo

import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import com.park254.app.park254.models.Booking
import com.park254.app.park254.models.Payment
import com.park254.app.park254.models.User
import com.park254.app.park254.network.RetrofitApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PaymentVerificationViewModel @Inject
constructor(
        val retrofitApiService: RetrofitApiService,
        val job: Job,
        val threadPool : ExecutorCoroutineDispatcher
) : ViewModel(), CoroutineScope {


    override val coroutineContext: CoroutineContext
        get() =   Dispatchers.Default + job

    var payment :Payment? = null
    var booking : Booking? = null
    var user : User? = null

}