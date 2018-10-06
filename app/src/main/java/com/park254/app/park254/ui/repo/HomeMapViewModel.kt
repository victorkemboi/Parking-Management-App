package com.park254.app.park254.ui.repo

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.support.design.widget.Snackbar
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment
import com.google.android.gms.location.zzz
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polyline
import com.google.maps.DirectionsApi
import com.park254.app.park254.models.LotResponse
import com.park254.app.park254.network.RetrofitApiService
import kotlinx.coroutines.experimental.*
import javax.inject.Inject
import kotlin.coroutines.experimental.CoroutineContext
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.LatLng
import com.google.maps.model.TravelMode
import com.park254.app.park254.R
import org.joda.time.DateTime
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil


class HomeMapViewModel @Inject
constructor(
        val retrofitApiService: RetrofitApiService,
        val job: Job,
        val threadPool : ExecutorCoroutineDispatcher
) : ViewModel(), CoroutineScope {


    override val coroutineContext: CoroutineContext
        get() =   Dispatchers.Default + job

    var map: GoogleMap? = null
    var userDestinationMarker : Marker? = null
    lateinit var mFusedLocationProviderClient : FusedLocationProviderClient
    lateinit var mLocationRequest : LocationRequest
    lateinit var  locationCallback : LocationCallback
     var routePolyline: Polyline? = null

    lateinit var googleApiClient : GoogleApiClient

   val nearByParkingLotsMarkersHashMap =HashMap<Marker, LotResponse>()

    val destinationNearByParkingLotsMarkersHashMap = HashMap<Marker, LotResponse>()


    private fun getGeoContext(context: Context) : GeoApiContext {
        return GeoApiContext.Builder()
                .apiKey(context.getString(R.string.directions_apikey))
                .build()
    }
     lateinit var mGpsSwitchStateReceiver : BroadcastReceiver

    lateinit var locationSnackbar :Snackbar

    var deviceLocationMarker: Marker?  = null

    var deviceLocation = MutableLiveData<LatLng>()

    var autocompleteFragment : SupportPlaceAutocompleteFragment? = null

     fun startDiractionsRequest(context: Context, origin: LatLng, destination:LatLng): DirectionsResult {
        val now =  DateTime()

        return DirectionsApi.newRequest(getGeoContext(context))
                .mode(TravelMode.DRIVING).origin(origin)
                .destination(destination).departureTime(now)
                .await()
    }



     fun addPolyline(results: DirectionsResult): Polyline? {
         if(results.routes.isNotEmpty()){
             val decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.encodedPath)
             routePolyline = map?.addPolyline(PolylineOptions().addAll(decodedPath))
             return  routePolyline
         }

         return null
    }

}