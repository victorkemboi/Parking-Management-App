package com.park254.app.park254.ui.repo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.support.v4.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.park254.app.park254.models.Lot
import com.park254.app.park254.models.LotResponse
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.ui.HomeActivity
import com.park254.app.park254.ui.fragments.HomeFragment
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import kotlinx.coroutines.experimental.*
import java.util.ArrayList
import javax.inject.Inject
import kotlin.coroutines.experimental.CoroutineContext

class HomeViewModel @Inject
constructor(
        val retrofitApiService: RetrofitApiService,
        val job: Job,
        val threadPool : ExecutorCoroutineDispatcher
) : ViewModel(), CoroutineScope{


    override val coroutineContext: CoroutineContext
        get() =   Dispatchers.Default + job


    var address = ""
    var longitude = 0.0
    var latitude  = 0.0

    var parsedLot:LotResponse? = null


    var homeMapFragment : Fragment? = null


}