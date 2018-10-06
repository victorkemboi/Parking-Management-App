package com.park254.app.park254.ui.repo

import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import com.park254.app.park254.models.Payment
import com.park254.app.park254.network.RetrofitApiService
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.ExecutorCoroutineDispatcher
import kotlinx.coroutines.experimental.Job
import javax.inject.Inject
import kotlin.coroutines.experimental.CoroutineContext

class PaymentVerificationViewModel @Inject
constructor(
        val retrofitApiService: RetrofitApiService,
        val job: Job,
        val threadPool : ExecutorCoroutineDispatcher
) : ViewModel(), CoroutineScope {


    override val coroutineContext: CoroutineContext
        get() =   Dispatchers.Default + job

    var payment :Payment? = null

}