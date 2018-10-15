package com.park254.app.park254.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.ColorInt
import android.support.annotation.NonNull
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.AppCompatImageButton
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.models.LotResponse
import com.park254.app.park254.models.NearByParkingLotRequest
import com.park254.app.park254.ui.HomeActivity
import com.park254.app.park254.ui.LotInfoActivity
import com.park254.app.park254.ui.repo.HomeMapViewModel
import com.park254.app.park254.utils.UtilityClass
import com.park254.app.park254.utils.UtilityClass.REQUEST_CHECK_SETTINGS
import com.park254.app.park254.utils.UtilityClass.REQUEST_LOCATION_PERMISSION_FOR_GET_DEVICE_LOCATION
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import kotlinx.android.synthetic.main.home_map_view_fragment.*
import kotlinx.coroutines.experimental.*
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.experimental.CoroutineContext


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HomeFragment : Fragment(), OnMapReadyCallback,  PlaceSelectionListener, CoroutineScope {

    //class variables

    private var param1: String? = null
    private var param2: String? = null

    @Inject
    lateinit var viewModel : HomeMapViewModel
    @Inject
    lateinit var job: Job

    @Inject
    lateinit var threadPool : ExecutorCoroutineDispatcher

    private  var mapView : MapView? = null

    private var listener: OnFragmentInteractionListener? = null

    override val coroutineContext: CoroutineContext
        get() =   Dispatchers.Default + job

    lateinit var mGpsSwitchStateReceiver : BroadcastReceiver


    // Class methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as App).applicationInjector.inject(this)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

       // (activity as HomeActivity).viewModel.setParkingLotsLocationList(this)

        viewModel.mFusedLocationProviderClient = LocationServices
                .getFusedLocationProviderClient(activity as HomeActivity)

        viewModel.googleApiClient =
                GoogleApiClient.Builder(this.context!!)
                .addApi(LocationServices.API).build()

        viewModel.mLocationRequest =  LocationRequest.create()

        viewModel.locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                //Do what you want with the position here

                for (location in locationResult.locations) {
                    if (location != null) {

                        viewModel.deviceLocation.postValue(
                                com.google.maps.model.LatLng(location.latitude,location.longitude)
                        )

                        break
                    }
                }

            }
        }

        viewModel.locationSnackbar =  Snackbar.make((activity as HomeActivity).window.decorView.rootView,
                "Please turn on location for improved experience!", Snackbar.LENGTH_INDEFINITE).withColor(R.color.red_600)

        mGpsSwitchStateReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, intent: Intent?) {
                Log.d("mGps Receiver:","On receive")
                if (intent != null) {
                    Log.d("mGps Receiver:","On receive, intent not null")
                    if (LocationManager.PROVIDERS_CHANGED_ACTION == intent.action) {
                        // Make an action or refresh an already managed state.
                        Log.d("mGps Receiver:","On receive, intent not null, provider changed")
                        if(isLocationEnabled() && isLocationAccuracyModeHigh()){
                         //   requestLocation()

                            if(viewModel.deviceLocationMarker!=null){
                                viewModel.deviceLocationMarker?.remove()
                            }
                            getDeviceLocation()

                            viewModel.locationSnackbar.dismiss()
                        }else{
                            if(viewModel.deviceLocationMarker!=null){
                                viewModel.deviceLocationMarker?.remove()
                            }
                            viewModel.routePolyline = null
                            viewModel.locationSnackbar.show()

                        }
                    }else{
                        Log.d("mGps Receiver:","On receive, location provider not changed")
                    }
                }else{
                    Log.d("mGps Receiver:","On receive, intent null")
                }
            }

        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

      val view = inflater.inflate(R.layout.home_map_view_fragment, container, false)

      mapView = view?.findViewById(R.id.home_lyt_map_view) as MapView
      mapView?.onCreate(savedInstanceState)
      mapView?.getMapAsync(this)

      return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val mapViewBundle : Bundle
        if (savedInstanceState != null){
            mapViewBundle = savedInstanceState

            home_lyt_map_view.onCreate(mapViewBundle)
        }

        // Retrieve the PlaceAutocompleteFragment.
       viewModel.autocompleteFragment  = childFragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as SupportPlaceAutocompleteFragment

        val searchEditText = viewModel.autocompleteFragment!!.view?.findViewById<TextView>(R.id.place_autocomplete_search_input)

        searchEditText?.hint = "Where to?"
        searchEditText?.setHintTextColor( ContextCompat.getColor(this.context!!, R.color.grey_60))
        viewModel.autocompleteFragment!!.setOnPlaceSelectedListener(this)

        viewModel.autocompleteFragment!!.view?.findViewById<AppCompatImageButton>(R.id.place_autocomplete_clear_button)?.setOnClickListener{ view ->

            viewModel.userDestinationMarker?.remove()

            viewModel.autocompleteFragment!!.view?.findViewById<TextView>(R.id.place_autocomplete_search_input)?.text = ""
            searchEditText?.hint = "Where to?"

            viewModel.autocompleteFragment!!.view?.findViewById<AppCompatImageButton>(R.id.place_autocomplete_clear_button)?.visibility = View.INVISIBLE
            viewModel.routePolyline?.remove()
            viewModel.deviceLocation.observe(this@HomeFragment, Observer<com.google.maps.model.LatLng> {
                deviceLocation-> run{
                val userLocation = LatLng(deviceLocation?.lat!!, deviceLocation.lng)
                showDeviceLocation(userLocation)
            }
            })

        }

        my_location_card_view.setOnClickListener {

            turnOnLocation()

        }

    }

    override fun onMapReady(gMap: GoogleMap?) {

        viewModel.map = gMap

        defaultMapLocation()

        turnOnLocation()

    }

    override fun onPlaceSelected(place: Place?) {
        viewModel.autocompleteFragment!!.view?.findViewById<AppCompatImageButton>(R.id.place_autocomplete_clear_button)?.visibility = View.VISIBLE

        viewModel.userDestinationMarker?.remove()
        val destinationLocation = place?.latLng

        val nearByParkingLotRequest = NearByParkingLotRequest()
        nearByParkingLotRequest.latitude = destinationLocation!!.latitude
        nearByParkingLotRequest.longitude = destinationLocation.longitude
        nearByParkingLotRequest.count = 5
        setNearByParkingLots(nearByParkingLotRequest,true)

        viewModel.userDestinationMarker =  viewModel.map?.addMarker(
                MarkerOptions().position(destinationLocation)
                        .icon(BitmapDescriptorFactory.fromBitmap(
                createCustomMarker(activity as HomeActivity, place.name as String, R.drawable.ic_destination,R.layout.destination_marker_layout))) )
        viewModel. userDestinationMarker?.isVisible = true

        val destination = com.google.maps.model.LatLng(place.latLng?.latitude!!, place.latLng?.longitude!!)
        val deviceLocation = viewModel.deviceLocation.value
        try {
            val polyLine =    viewModel.addPolyline(viewModel.startDiractionsRequest(context!!, deviceLocation!!,destination))

            if (polyLine != null) {
                moveToBounds( polyLine)
            }
        }catch (e: KotlinNullPointerException){
            turnOnLocation()
        }




     }

    override fun onError(status: Status?) {

        viewModel.userDestinationMarker?.remove()
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION_FOR_GET_DEVICE_LOCATION){

            turnOnLocation()
            Log.w("onActivityResult: ", "REQUEST_LOCATION_PERMISSION_FOR_GET_DEVICE_LOCATION results")
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        Log.w("Activity Result:", "REQUEST_CHECK_SETTINGS")
            if(requestCode == REQUEST_CHECK_SETTINGS){

                turnOnLocation()
            }

    }

    //custom functions
    private fun createCustomMarker(context: Context, _name: String,drawable: Int, layout: Int): Bitmap {

        val marker = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(layout, null)

        val markerImage = marker.findViewById(R.id.park_marker_img)  as ImageView
        markerImage.setImageResource(drawable)
        val txt_name = marker.findViewById(R.id.name) as TextView
        txt_name.text = _name

        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        marker.layoutParams = ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT)
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(marker.measuredWidth, marker.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        marker.draw(canvas)

        return bitmap
    }

    private fun createUserLocationMarker(context: Context, layout: Int): Bitmap {

        val marker = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(layout, null)

        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        marker.layoutParams = ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT)
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(marker.measuredWidth, marker.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        marker.draw(canvas)

        return bitmap
    }

    private fun checkIfLocationPermissionIsGranted(): Boolean{

        if (ContextCompat.checkSelfPermission(this.context!!,Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            &&
        ContextCompat.checkSelfPermission(this.context!!,Manifest.permission.ACCESS_COARSE_LOCATION)
        == PackageManager.PERMISSION_GRANTED

        ) {
            return true
        }
        return false
    }

    private fun requestLocationPermission(){


        if (!checkIfLocationPermissionIsGranted()) {

            requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_LOCATION_PERMISSION_FOR_GET_DEVICE_LOCATION)

        }


    }

    private fun turnOnLocation(){
        if (checkIfLocationPermissionIsGranted()){
            if(isLocationEnabled() && isLocationAccuracyModeHigh()){

                setDeviceLocation()

            }else{
                //direct users to turn on location in settings

                onDisplayLocationSettingsRequest()
            }
        }else{
            requestLocationPermission()
        }

    }

    private fun setDeviceLocation(){
        getDeviceLocation()

        viewModel.deviceLocation.observe(this@HomeFragment, Observer<com.google.maps.model.LatLng> {
            deviceLocation-> run{
            val userLocation = LatLng(deviceLocation?.lat!!, deviceLocation.lng)
            showDeviceLocation(userLocation)
            val nearByParkingLotRequest = NearByParkingLotRequest()
            nearByParkingLotRequest.latitude = userLocation.latitude
            nearByParkingLotRequest.longitude = userLocation.longitude
            nearByParkingLotRequest.count = 5
            setNearByParkingLots(nearByParkingLotRequest,false)
        }
        })
    }

    private fun getDeviceLocation() {
        try {

               if (checkIfLocationPermissionIsGranted()) {

                   val   locationResult : Task<Location> =  viewModel.mFusedLocationProviderClient.lastLocation
                   locationResult.addOnCompleteListener { task ->
                       run {
                           if (task.isSuccessful && task.result!=null) {
                               Log.d("Location:","getDeviceLocation task not null")
                               // Set the map's camera position to the current location of the device.
                               val location = task.result

                               viewModel.deviceLocation.postValue(
                                       com.google.maps.model.LatLng(location.latitude,
                                               location.longitude)
                               )

                           }else{
                               Log.d("Location:","getDeviceLocation task  null, request location")

                               requestLocation()

                           }
                       }
                   }
               }
               else{

                   ActivityCompat.requestPermissions(activity as HomeActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                           REQUEST_LOCATION_PERMISSION_FOR_GET_DEVICE_LOCATION)

               }

        } catch ( e:SecurityException) {
            Log.e("Exception: %s", e.message)
        }

}

    private fun showDeviceLocation(location: LatLng){
        if (viewModel.deviceLocationMarker != null){
            viewModel.deviceLocationMarker!!.position = location
        }else{
            viewModel.deviceLocationMarker = viewModel. map?.addMarker(MarkerOptions().position(location).icon(BitmapDescriptorFactory.fromBitmap(
                    createUserLocationMarker(this.context!!, R.layout.user_current_location_marker_layout))))
        }


        val center: CameraUpdate=
                CameraUpdateFactory.newLatLng(location)
        val zoom: CameraUpdate=CameraUpdateFactory.zoomTo((15).toFloat())

        viewModel.map?.moveCamera(center)
        viewModel.map?.animateCamera(zoom)


    }

    private fun isLocationAccuracyModeHigh(): Boolean{

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            var locationMode  = 0
            try {
                locationMode = Settings.Secure.getInt(this.context?.contentResolver,Settings.Secure.LOCATION_MODE)

            } catch ( e: Settings.SettingNotFoundException) {
                e.printStackTrace()
            }

            (locationMode != Settings.Secure.LOCATION_MODE_OFF && locationMode == Settings.Secure.LOCATION_MODE_HIGH_ACCURACY)


        }else{
            val locationProviders = Settings.Secure.getString(this.context?.contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
            !TextUtils.isEmpty(locationProviders)
        }

    }

    private  fun isLocationEnabled(): Boolean{
        var locationMode = 0
        val locationProviders : String
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = android.provider.Settings.Secure.getInt(context?.contentResolver, android.provider.Settings.Secure.LOCATION_MODE)
            }catch (e:android.provider.Settings.SettingNotFoundException){
                e.printStackTrace()
                return false
            }
            return locationMode != android.provider.Settings.Secure.LOCATION_MODE_OFF
        }else{
            locationProviders = android.provider.Settings.Secure.getString(context?.contentResolver, android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
            return !TextUtils.isEmpty(locationProviders)
        }

    }

    private fun defaultMapLocation(){

        val  nairobi= LatLng(-1.28333 , 36.81667)
        val center: CameraUpdate=
                CameraUpdateFactory.newLatLng(nairobi)
        val zoom: CameraUpdate=CameraUpdateFactory.zoomTo((15).toFloat())

        viewModel.map?.moveCamera(center)
        viewModel.map?.animateCamera(zoom)
    }

    private  fun onParkingLotMarkerClicked(marker: Marker){

        (activity as HomeActivity).viewModel.parsedLot = viewModel.nearByParkingLotsMarkersHashMap[marker]

        if((activity as HomeActivity).viewModel.parsedLot!=null){

            startActivity(
                    Intent(this@HomeFragment.context, LotInfoActivity::class.java))
        }
    }

    private fun onDisplayLocationSettingsRequest() {
        viewModel.googleApiClient.connect()

    val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 10000 / 2

    val builder =  LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    builder.setAlwaysShow(true)

    val result1 = LocationServices.getSettingsClient(activity as HomeActivity).checkLocationSettings(builder.build())

        result1.addOnCompleteListener {
            task -> run {
            try {
                task.getResult(ResolvableApiException::class.java)
                //getDeviceLocation()
            }catch (e: ResolvableApiException){
                when (e.statusCode) {
                    LocationSettingsStatusCodes.SUCCESS -> Log.i("OK" , "All location settings are satisfied.")

                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.i("Resolution required: ", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ")

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            e.startResolutionForResult((activity as HomeActivity), REQUEST_CHECK_SETTINGS)
                        } catch (e: IntentSender.SendIntentException) {
                            Log.w("Error:", "PendingIntent unable to execute request.")
                        }

                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->
                        Log.i("", "Location settings are inadequate, and cannot be fixed here. Dialog not created.")

                    else -> Log.i("Error", "Location settings are inadequate, and cannot be fixed here. Dialog not created.")
                }
            }


        }
        }

    }

    @SuppressLint("MissingPermission")
    private  fun requestLocation(){

       viewModel.mLocationRequest.interval = 60000
        viewModel.mLocationRequest.fastestInterval = 5000
        viewModel.mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

                //LocationServices.getFusedLocationProviderClient(this.context!!).requestLocationUpdates()
        viewModel.mFusedLocationProviderClient.requestLocationUpdates(viewModel.mLocationRequest, viewModel.locationCallback, null)
    }

    private fun Snackbar.withColor(@ColorInt colorInt: Int): Snackbar{
        this.view.setBackgroundColor(ContextCompat.getColor(this.context, colorInt))
        return this
    }

    private fun moveToBounds( p:Polyline) {

    val builder =  LatLngBounds.Builder()

    for(point in p.points){
        builder.include(point)
    }
    val bounds = builder.build()
    val padding = 40  // offset from edges of the map in pixels
    val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
    viewModel.map?.animateCamera(cu)
}

    private fun setNearByParkingLots(nearByParkingLotRequest: NearByParkingLotRequest,destinationLocation:Boolean){
        launch {
            withContext(threadPool) {

                (activity as HomeActivity).viewModel.retrofitApiService.getNearByParkingLots(nearByParkingLotRequest)
                        .observe(this@HomeFragment, Observer<ApiResponse<List<LotResponse>>> { response ->
                    run {
                        if (response != null && response.isSuccessful) {
                            val parkingLotList = response.body as ArrayList<LotResponse>

                            for (parkingLot in parkingLotList) {

                                val parkingLotLocation = LatLng(parkingLot.latitude, parkingLot.longitude)
                                val marker = viewModel.map?.addMarker(
                                        MarkerOptions().position(parkingLotLocation)
                                                .icon(BitmapDescriptorFactory
                                                        .fromBitmap(
                                                                createCustomMarker(activity as HomeActivity, parkingLot.name, R.drawable.park_marker, R.layout.park_custom_marker_layout)
                                                        )))
                                if (marker != null) {
                                    if (destinationLocation){
                                        for (parkingLotMarker in  viewModel.destinationNearByParkingLotsMarkersHashMap){
                                            parkingLotMarker.key.remove()
                                            viewModel.destinationNearByParkingLotsMarkersHashMap.remove(parkingLotMarker.key)

                                        }

                                        viewModel.destinationNearByParkingLotsMarkersHashMap[marker] = parkingLot
                                    }else{
                                        viewModel.nearByParkingLotsMarkersHashMap[marker] = parkingLot
                                    }

                                }

                            }


                            viewModel.map?.setOnMarkerClickListener {

                                marker ->
                                run {

                                    onParkingLotMarkerClicked(marker)

                                }
                                return@setOnMarkerClickListener false
                            }

                        }
                    }
                }
                )

            }}
    }


    // Fragment override functions section.
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {

        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                HomeFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle= outState.getBundle(UtilityClass.MAP_VIEW_BUNDLE_KEY)
        if (mapViewBundle == null){
            mapViewBundle = Bundle()
            outState.putBundle(UtilityClass.MAP_VIEW_BUNDLE_KEY, mapViewBundle)
        }

        mapView!!.onSaveInstanceState(mapViewBundle)

    }

    override fun onPause() {

        super.onPause()
        if (mapView !=null){
            mapView!!.onPause()
        }

        viewModel.mFusedLocationProviderClient.removeLocationUpdates(viewModel.locationCallback)
        context!!.unregisterReceiver(mGpsSwitchStateReceiver)
        viewModel.deviceLocationMarker = null



    }

    override fun onLowMemory() {
        super.onLowMemory()
        if (mapView !=null){
            mapView!!.onLowMemory()
        }

    }

    override fun onStart() {
        super.onStart()

        if (mapView !=null){
            mapView!!.onStart()
        }

    }

    override fun onResume() {

        super.onResume()
        context!!.registerReceiver(mGpsSwitchStateReceiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
        if (mapView !=null){
            mapView!!.onResume()
        }
        //LocalBroadcastManager.getInstance(context!!).registerReceiver(viewModel.mGpsSwitchStateReceiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))



    }

    override fun onStop() {
        super.onStop()
        if (mapView !=null){
            mapView!!.onStop()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (mapView !=null){
            mapView!!.onDestroy()
        }

    }



}


//name, booking, cost, payment