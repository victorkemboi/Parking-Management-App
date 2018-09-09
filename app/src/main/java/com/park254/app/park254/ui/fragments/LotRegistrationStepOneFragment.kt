package com.park254.app.park254.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.park254.app.park254.R
import com.park254.app.park254.ui.RegistrationInterface
import com.park254.app.park254.ui.repo.ParkingLotRegistrationViewModel
import com.park254.app.park254.utils.UtilityClass
import com.schibstedspain.leku.LocationPickerActivity
import kotlinx.android.synthetic.main.fragment_lot_registration_step_one.*
import javax.inject.Inject

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
class LotRegistrationStepOneFragment : Fragment(),RegistrationInterface {



    override fun validateInputs():Boolean {

        if (input_parking_lot_name.text.toString().trim { it <= ' ' }.isEmpty()) run {
            input_layout_parking_lot_name.error = "Enter parking lot name!"
            UtilityClass.requestFocus(input_parking_lot_name, activity!!.window)
            return false
        }
        if (input_street_name.text.toString().trim { it <= ' ' }.isEmpty()) run {
            input_layout_street_name.error = "Enter street name!"
            UtilityClass.requestFocus(input_layout_street_name, activity!!.window)
            return false
        }
        if (viewModel.lot.longitude == 0.0 && viewModel.lot.latitude == 0.0) run {
            input_layout_location.error = "Select Location!"
            UtilityClass.requestFocus(input_layout_street_name, activity!!.window)
            return false
        }


        return true
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        input_location.setOnClickListener {
            val locationPickerIntent = LocationPickerActivity.Builder()
                    .withLocation(41.4036299, 2.1743558)
                    .withGeolocApiKey("<PUT API KEY HERE>")
                    .withSearchZone("es_ES")
                    .shouldReturnOkOnBackPressed()
                    .withStreetHidden()
                    .withCityHidden()
                    .withZipCodeHidden()
                    .withSatelliteViewHidden()
                    .withGooglePlacesEnabled()
                    .withGoogleTimeZoneEnabled()
                    .withVoiceSearchHidden()
                    .build(activity!!.applicationContext)

            startActivityForResult(locationPickerIntent, viewModel.MAP_BUTTON_REQUEST_CODE)
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
}
