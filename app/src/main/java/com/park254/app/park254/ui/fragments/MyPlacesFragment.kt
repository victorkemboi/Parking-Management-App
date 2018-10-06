package com.park254.app.park254.ui.fragments

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.models.LotResponse
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.ui.HomeActivity
import com.park254.app.park254.ui.LotInfoActivity
import com.park254.app.park254.ui.adapters.HomeListAdapter
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import kotlinx.android.synthetic.main.fragment_main_home.*
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
 * [MyPlacesFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MyPlacesFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MyPlacesFragment : Fragment(), CoroutineScope, SwipeRefreshLayout.OnRefreshListener

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

    private val homeFragmentContext: MyPlacesFragment = this

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




    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_main_home, container, false)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)




        home_swipe_container.setOnRefreshListener(this)
        home_swipe_container.setColorSchemeColors(
                resources.getColor( android.R.color.holo_green_dark),
                resources.getColor(android.R.color.holo_red_dark)  ,
                resources.getColor(android.R.color.holo_blue_dark)   ,
                resources.getColor(android.R.color.holo_orange_dark)   )


        setHomePage()


    }
    override fun onRefresh() {

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



    override fun onDestroy() {
        super.onDestroy()
        (activity as HomeActivity). viewModel.address = ""
        (activity as HomeActivity).viewModel.latitude = 0.0
        (activity as HomeActivity).viewModel.longitude = 0.0
    }


    override fun onResume() {
        super.onResume()


        if ((activity as HomeActivity).viewModel.address != "" && (activity as HomeActivity).viewModel.latitude !=0.0 && (activity as HomeActivity).viewModel.longitude!=0.0){

            //txt_view_home_location.setText((activity as HomeActivity).viewModel.address)

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
         * @return A new instance of fragment MyPlacesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                MyPlacesFragment().apply {
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



    private fun setHomePage(){

        if (!home_swipe_container.isRefreshing){
            home_swipe_container.isRefreshing = true
        }
        launch {
            withContext(threadPool){

                retrofitApiService.getParkingLots().observe(homeFragmentContext, Observer<ApiResponse<List<LotResponse>>> {
                    response->
                    if (response != null && response.isSuccessful) {
                        home_packing_lots_recycler_view.layoutManager = LinearLayoutManager(activity!!.applicationContext)

                        home_packing_lots_recycler_view.setHasFixedSize(false)

                        // Log.d("Resp",response.body.toString())
                        mAdapter = HomeListAdapter(activity as HomeActivity, response.body as ArrayList<LotResponse>)

                        home_packing_lots_recycler_view.adapter = mAdapter

                        mAdapter!!.onItemClick = {
                            lot ->

                            (activity as HomeActivity).viewModel.parsedLot = lot
                            startActivity(
                                    Intent(this@MyPlacesFragment.context, LotInfoActivity::class.java))
                        }

                    }else{
                        if (response != null) {
                            // Log.d("Resp",response.body.toString())
                        }
                    }

                    this@MyPlacesFragment.run {
                        if (home_swipe_container.isRefreshing){
                            home_swipe_container.isRefreshing = false
                        }
                    }


                })
            }
        }
    }


}