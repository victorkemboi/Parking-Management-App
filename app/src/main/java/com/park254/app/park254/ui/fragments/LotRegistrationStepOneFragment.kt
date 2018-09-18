package com.park254.app.park254.ui.fragments

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.park254.app.park254.R
import com.park254.app.park254.ui.repo.ParkingLotRegistrationViewModel
import com.park254.app.park254.utils.UtilityClass
import kotlinx.android.synthetic.main.fragment_lot_registration_step_one.*
import javax.inject.Inject
import android.content.Intent
import android.location.Address
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.park254.app.park254.models.Lot
import com.park254.app.park254.ui.ParkingLotRegistrationActivity
import com.park254.app.park254.ui.UpdateInfoActivity
import com.schibstedspain.leku.*
import kotlinx.android.synthetic.main.activity_parking_lot_registration.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LotRegistrationStepOneFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LotRegistrationStepOneFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class LotRegistrationStepOneFragment : Fragment() {


    @Inject
    lateinit var viewModel: ParkingLotRegistrationViewModel

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

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
        return inflater.inflate(R.layout.fragment_lot_registration_step_one, container, false)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == UtilityClass.MAP_BUTTON_REQUEST_CODE) {


            try {
                if (resultCode == Activity.RESULT_OK && data != null) {

                    // Log.d("RESULT****", data.getDoubleExtra(LATITUDE, 0.0).toString())
                    (activity as ParkingLotRegistrationActivity).viewModel.lot.latitude = data.getDoubleExtra(LATITUDE, 0.0)
                    //Log.d("LATITUDE****", (activity as ParkingLotRegistrationActivity).viewModel.lot.latitude.toString())
                    (activity as ParkingLotRegistrationActivity).viewModel.lot.longitude = data.getDoubleExtra(LONGITUDE, 0.0)
                    // Log.d("LONGITUDE****", (activity as ParkingLotRegistrationActivity).viewModel.lot.longitude.toString())
                    (activity as ParkingLotRegistrationActivity).viewModel.addresss = data.getStringExtra(LOCATION_ADDRESS)
                    //Log.d("ADDRESS****", address.toString())

                }
                else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.d("RESULT****", "CANCELLED")
                }

            }catch (e:ClassCastException){
                if (resultCode == Activity.RESULT_OK && data != null) {

                    // Log.d("RESULT****", data.getDoubleExtra(LATITUDE, 0.0).toString())
                    (activity as UpdateInfoActivity).viewModel.lot.latitude = data.getDoubleExtra(LATITUDE, 0.0)
                    //Log.d("LATITUDE****", (activity as ParkingLotRegistrationActivity).viewModel.lot.latitude.toString())
                    (activity as UpdateInfoActivity).viewModel.lot.longitude = data.getDoubleExtra(LONGITUDE, 0.0)
                    // Log.d("LONGITUDE****", (activity as ParkingLotRegistrationActivity).viewModel.lot.longitude.toString())
                    (activity as UpdateInfoActivity).viewModel.addresss = data.getStringExtra(LOCATION_ADDRESS)
                    //Log.d("ADDRESS****", address.toString())

                }
                else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.d("RESULT****", "CANCELLED")
                }
            }


        }

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        input_location.setOnClickListener {
            val locationPickerIntent = LocationPickerActivity.Builder()
                    .withLocation(41.4036299, 2.1743558)
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
                    .build(activity!!.applicationContext)

            startActivityForResult(locationPickerIntent, UtilityClass.MAP_BUTTON_REQUEST_CODE)
        }



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
         * @return A new instance of fragment LotRegistrationStepOneFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                LotRegistrationStepOneFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onResume() {
        super.onResume()
        fillViewValues()
        setAddress()
    }

    fun fillViewValues(){



        try {
            if ((activity as ParkingLotRegistrationActivity).viewModel.lot.name!=""){
                // Log.d("Fill Values","start")
                input_parking_lot_name.setText((activity as ParkingLotRegistrationActivity).viewModel.lot.name)
            }
            if ((activity as ParkingLotRegistrationActivity).viewModel.lot.streetName!=""){
                input_street_name.setText((activity as ParkingLotRegistrationActivity).viewModel.lot.name)
            }
            if ((activity as ParkingLotRegistrationActivity).viewModel.lot.parkingSpaces!=0){
                input_picker_parking_spaces.value = (activity as ParkingLotRegistrationActivity).viewModel.lot.parkingSpaces
            }
            if ((activity as ParkingLotRegistrationActivity).viewModel.lot.email != "") {
                // Log.d("Fill Values","start")
                input_email.setText((activity as ParkingLotRegistrationActivity).viewModel.lot.email)
            }
            if ((activity as ParkingLotRegistrationActivity).viewModel.lot.contactNumber != "") {
                input_parking_lot_contact_no.setText(   (activity as ParkingLotRegistrationActivity).viewModel.lot.contactNumber.toString())
            }
            if ((activity as ParkingLotRegistrationActivity).viewModel.lot.paybillNumber != "") {
                input_paybill_no.setText((activity as ParkingLotRegistrationActivity).viewModel.lot.paybillNumber)
            }
        }catch (e:ClassCastException){

            if ((activity as UpdateInfoActivity).viewModel.lot.name!=""){
                // Log.d("Fill Values","start")
                input_parking_lot_name.setText((activity as UpdateInfoActivity).viewModel.lot.name)
            }
            if ((activity as UpdateInfoActivity).viewModel.lot.streetName!=""){
                input_street_name.setText((activity as UpdateInfoActivity).viewModel.lot.name)
            }
            if ((activity as UpdateInfoActivity).viewModel.lot.parkingSpaces!=0){
                input_picker_parking_spaces.value = (activity as UpdateInfoActivity).viewModel.lot.parkingSpaces
            }
            if ((activity as UpdateInfoActivity).viewModel.lot.email != "") {
                // Log.d("Fill Values","start")
                input_email.setText((activity as UpdateInfoActivity).viewModel.lot.email)
            }
            if ((activity as UpdateInfoActivity).viewModel.lot.contactNumber != "") {
                input_parking_lot_contact_no.setText(   (activity as UpdateInfoActivity).viewModel.lot.contactNumber.toString())
            }
            if ((activity as UpdateInfoActivity).viewModel.lot.paybillNumber != "") {
                input_paybill_no.setText((activity as UpdateInfoActivity).viewModel.lot.paybillNumber)
            }
        }



    }

    fun setAddress(){

        try {


        if ((activity as ParkingLotRegistrationActivity).viewModel.addresss!=""){

            input_layout_location_display.visibility = View.VISIBLE
            tv_display_location.text = (activity as ParkingLotRegistrationActivity).viewModel.addresss
            img_input_location.setImageResource(R.drawable.ic_edit_location)
            tv_input_location.text = "Change Location"


        }else{
            input_layout_location_display.visibility = View.GONE
            tv_display_location.text = ""
            img_input_location.setImageResource(R.drawable.ic_add_location)
            tv_input_location.text = "Pick Location"
        }


    }catch (e:ClassCastException){
            if ((activity as UpdateInfoActivity).viewModel.addresss!=""){

                input_layout_location_display.visibility = View.VISIBLE
                tv_display_location.text = (activity as UpdateInfoActivity).viewModel.addresss
                img_input_location.setImageResource(R.drawable.ic_edit_location)
                tv_input_location.text = "Change Location"


            }else{
                input_layout_location_display.visibility = View.GONE
                tv_display_location.text = ""
                img_input_location.setImageResource(R.drawable.ic_add_location)
                tv_input_location.text = "Pick Location"
            }
        }
    }
}
