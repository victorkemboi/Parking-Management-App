package com.park254.app.park254.ui.fragments

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.glide.slider.library.Tricks.ViewPagerEx
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.park254.app.park254.App

import com.park254.app.park254.R
import com.park254.app.park254.models.Lot
import com.park254.app.park254.models.LotResponse
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.ui.HomeActivity
import com.park254.app.park254.ui.LotInfoActivity
import com.park254.app.park254.ui.adapters.HomeListAdapter
import com.park254.app.park254.utils.Permissons
import com.park254.app.park254.utils.UtilityClass
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import com.schibstedspain.leku.LATITUDE
import com.schibstedspain.leku.LOCATION_ADDRESS
import com.schibstedspain.leku.LONGITUDE
import com.schibstedspain.leku.LocationPickerActivity
import kotlinx.android.synthetic.main.fragment_main_home.*
import kotlinx.android.synthetic.main.include_cardview_search_bar.*
import kotlinx.coroutines.experimental.*
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.experimental.CoroutineContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MainHomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MainHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MainHomeFragment : Fragment(), CoroutineScope

{

    private var param1: String? = null
    private var param2: String? = null

    private var listener: OnFragmentInteractionListener? = null
    private var mAdapter: HomeListAdapter? = null

    @Inject
    lateinit var retrofitApiService: RetrofitApiService

    @Inject
    lateinit var job: Job

    @Inject
    lateinit var threadPool : ExecutorCoroutineDispatcher

    private val homeFragmentContext: MainHomeFragment = this

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override val coroutineContext: CoroutineContext
        get() =   Dispatchers.Default + job




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        (activity!!.application as App).applicationInjector.inject(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)


     //   (activity as HomeActivity).initToolBar("Home")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_home, container, false)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        home_packing_lots_recycler_view.layoutManager = LinearLayoutManager(activity!!.applicationContext)

        home_packing_lots_recycler_view.setHasFixedSize(false)






        btn_search_location.setOnClickListener{launchSetLocationActivity()}
        search_bar.setOnClickListener{
            launchSetLocationActivity()
        }

        setHomePage()


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == UtilityClass.MAP_BUTTON_REQUEST_CODE) {


            if (resultCode == Activity.RESULT_OK && data != null) {

                // Log.d("RESULT****", data.getDoubleExtra(LATITUDE, 0.0).toString())
                (activity as HomeActivity). viewModel.latitude = data.getDoubleExtra(LATITUDE, 0.0)
                //Log.d("LATITUDE****", (activity as ParkingLotRegistrationActivity).viewModel.requestLot.latitude.toString())
                (activity as HomeActivity).viewModel.longitude = data.getDoubleExtra(LONGITUDE, 0.0)
                // Log.d("LONGITUDE****", (activity as ParkingLotRegistrationActivity).viewModel.requestLot.longitude.toString())
                (activity as HomeActivity).viewModel.address = data.getStringExtra(LOCATION_ADDRESS)
                //Log.d("ADDRESS****", address.toString())

            }
            else if (resultCode == Activity.RESULT_CANCELED) {
            //    Log.d("RESULT****", "CANCELLED")
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as HomeActivity). viewModel.address = ""
        (activity as HomeActivity).viewModel.latitude = 0.0
        (activity as HomeActivity).viewModel.longitude = 0.0
    }


    override fun onResume() {
        super.onResume()


        if ((activity as HomeActivity).viewModel.address != "" && (activity as HomeActivity).viewModel.latitude !=0.0 && (activity as HomeActivity).viewModel.longitude!=0.0){

            txt_view_home_location.text = (activity as HomeActivity).viewModel.address

            //perform network requests
        }

        setHomePage()


    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainHomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                MainHomeFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onDestroyView() {
        home_packing_lots_recycler_view.adapter = null
        super.onDestroyView()
    }

    private fun launchSetLocationActivity(){

          /*  if ( ContextCompat.checkSelfPermission( activity!!.parent, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                fusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? ->


                        }}  */


    val locationPickerIntent = LocationPickerActivity.Builder()
            .withLocation(-1.28333 , 36.81667)
            .withGeolocApiKey("AIzaSyD3pEPtNFUNirTobNZciq7wDbxD_J0QXtw")
            .withSearchZone("en-US")
            .shouldReturnOkOnBackPressed()
            .withStreetHidden()
            .withCityHidden()
            .withZipCodeHidden()
            .withSatelliteViewHidden()
            .withGooglePlacesEnabled()
            .withGoogleTimeZoneEnabled()
            .withVoiceSearchHidden()
            .build(activity as HomeActivity)

    startActivityForResult(locationPickerIntent, UtilityClass.MAP_BUTTON_REQUEST_CODE)

    }

    private fun setHomePage(){
        launch {
            withContext(threadPool){
                retrofitApiService.getParkingLots().observe(homeFragmentContext, Observer<ApiResponse<List<LotResponse>>> {
                    response->
                    if (response != null && response.isSuccessful) {

                        // Log.d("Resp",response.body.toString())
                        mAdapter = HomeListAdapter(activity as HomeActivity, response.body as ArrayList<LotResponse>)

                        home_packing_lots_recycler_view.adapter = mAdapter

                        mAdapter!!.onItemClick = {
                            lot ->

                            (activity as HomeActivity).viewModel.parsedLot = lot
                            startActivity(
                                    Intent(this@MainHomeFragment.context, LotInfoActivity::class.java))
                        }

                    }else{
                        if (response != null) {
                            // Log.d("Resp",response.body.toString())
                        }
                    }


                })
            }
        }
    }


}
