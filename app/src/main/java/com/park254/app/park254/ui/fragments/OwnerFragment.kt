package com.park254.app.park254.ui.fragments

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.park254.app.park254.R
import com.park254.app.park254.models.Lot
import com.park254.app.park254.ui.HomeActivity
import com.park254.app.park254.ui.LotInfoActivity
import com.park254.app.park254.ui.OwnerLotInfoActivity
import com.park254.app.park254.ui.ParkingLotRegistrationActivity
import com.park254.app.park254.ui.adapters.HomeListAdapter
import com.park254.app.park254.ui.adapters.OwnerAdapter
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import kotlinx.android.synthetic.main.fragment_owner.*
import java.util.ArrayList

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
class OwnerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var mAdapter: OwnerAdapter? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        owner_packing_lots_recycler_view.layoutManager = LinearLayoutManager(activity!!.applicationContext)

        owner_packing_lots_recycler_view.setHasFixedSize(false)

        (activity as HomeActivity).retrofitApiService.getOwnedParkingLots().observe(this, Observer<ApiResponse<List<Lot>>> {
            response->
            if (response != null && response.isSuccessful) {

                //Log.d("Resp",response.body.toString())
                val items = response.body as ArrayList<Lot>
                if (items.isNotEmpty()){
                    owner_parking_lots.visibility = View.VISIBLE
                    owner_preview.visibility = View.GONE
                    mAdapter = OwnerAdapter( activity!!.applicationContext, items )

                    owner_packing_lots_recycler_view.adapter = mAdapter



                    mAdapter!!.onItemClick = {
                        lot ->
                        (activity as HomeActivity).viewModel.parsedLot = lot
                        //  Snackbar.make(booked_card_view, "Item " + lot.name + " clicked", Snackbar.LENGTH_SHORT).show()
                        startActivity(
                                Intent(this@OwnerFragment.context, OwnerLotInfoActivity::class.java))
                    }
                }else{

                }








            }else{
                if (response != null) {
                    owner_parking_lots.visibility = View.GONE
                    owner_preview.visibility = View.VISIBLE
                    Log.d("Resp",response.body.toString())
                }
            }


        })


    }

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

        val view: View = inflater.inflate(R.layout.fragment_owner, container, false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_add_parking_lot.setOnClickListener { startActivity(
                Intent(activity, ParkingLotRegistrationActivity::class.java))}
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

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
         * @return A new instance of fragment OwnerFragment.
         */
        // TODO: Rename and change types and number of parameters
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
