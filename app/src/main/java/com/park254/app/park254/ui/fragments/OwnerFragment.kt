package com.park254.app.park254.ui.fragments

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.models.LotResponse
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.ui.HomeActivity
import com.park254.app.park254.ui.OwnerLotInfoActivity
import com.park254.app.park254.ui.ParkingLotRegistrationActivity
import com.park254.app.park254.ui.adapters.OwnerListAdapter
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_owner.*
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [OwnerFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [OwnerFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class OwnerFragment : Fragment(), CoroutineScope, SwipeRefreshLayout.OnRefreshListener {


    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var mListAdapter: OwnerListAdapter? = null

    @Inject
    lateinit var retrofitApiService: RetrofitApiService

    @Inject
    lateinit var job: Job

    @Inject
    lateinit var threadPool : ExecutorCoroutineDispatcher

    private val ownerFragmentContext: OwnerFragment = this

    override val coroutineContext: CoroutineContext
        get() =   Dispatchers.Default + job






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_owner, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        owner_swipe_container.setOnRefreshListener(this)
        owner_swipe_container.setColorSchemeColors(
                ContextCompat.getColor( ownerFragmentContext.context!!,android.R.color.holo_green_dark),
                ContextCompat.getColor(ownerFragmentContext.context!!,android.R.color.holo_red_dark)  ,
                ContextCompat.getColor(ownerFragmentContext.context!!, android.R.color.holo_blue_dark)   ,
                ContextCompat.getColor(ownerFragmentContext.context!!, android.R.color.holo_orange_dark)   )

        owner_packing_lots_recycler_view.layoutManager = LinearLayoutManager(activity!!.applicationContext)

        owner_packing_lots_recycler_view.setHasFixedSize(false)

        btn_add_parking_lot.setOnClickListener { startActivity(
                Intent(activity, ParkingLotRegistrationActivity::class.java))}
        setOwner()

    }



    override fun onRefresh() {

        setOwner()
    }


    private fun setOwner(){


        if (!owner_swipe_container.isRefreshing){
            owner_swipe_container.isRefreshing = true
        }
        launch {
            withContext(threadPool) {

                retrofitApiService.getOwnedParkingLots().observe(ownerFragmentContext, Observer<ApiResponse<List<LotResponse>>> { response ->
                    if (response != null && response.isSuccessful) {

                        //Log.d("Resp",response.body.toString())
                        val items = response.body as ArrayList<LotResponse>
                        if (items.isNotEmpty()) {
                            owner_parking_lots.visibility = View.VISIBLE
                            owner_preview.visibility = View.GONE
                            mListAdapter = OwnerListAdapter(activity!!, items)

                            owner_packing_lots_recycler_view.adapter = mListAdapter


                            mListAdapter!!.onItemClick = { lot ->
                                (activity as HomeActivity).viewModel.parsedLot = lot
                                startActivity(
                                        Intent(this@OwnerFragment.context, OwnerLotInfoActivity::class.java))
                            }
                        } else {

                        }


                    } else {
                        if (response != null) {
                            owner_parking_lots.visibility = View.GONE
                            owner_preview.visibility = View.VISIBLE
                            //  Log.d("Resp", response.body.toString())
                        }
                    }


                    ownerFragmentContext.activity?.runOnUiThread {
                        if (owner_swipe_container.isRefreshing){
                            owner_swipe_container.isRefreshing = false
                        }
                    }




                })

            }
        }

    }


    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
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
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                OwnerFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

}
