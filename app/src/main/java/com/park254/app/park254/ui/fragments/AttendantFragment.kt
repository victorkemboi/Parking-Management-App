package com.park254.app.park254.ui.fragments

import android.arch.lifecycle.Observer
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.glide.slider.library.Animations.DescriptionAnimation
import com.glide.slider.library.SliderLayout
import com.glide.slider.library.SliderTypes.DefaultSliderView
import com.google.android.gms.common.api.Api
import com.google.firebase.auth.FirebaseAuth
import com.park254.app.park254.App

import com.park254.app.park254.R
import com.park254.app.park254.models.AvailableSpaceResponse
import com.park254.app.park254.models.AvailableSpaceUpdate
import com.park254.app.park254.models.Employee
import com.park254.app.park254.models.LotResponse
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.ui.HomeActivity
import com.park254.app.park254.ui.repo.AttendantViewModel
import com.park254.app.park254.utils.SharedPrefs
import com.park254.app.park254.utils.UtilityClass
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import kotlinx.android.synthetic.main.attendant_update_layout.*
import kotlinx.android.synthetic.main.attendant_update_layout.view.*
import kotlinx.android.synthetic.main.fragment_attendant.*
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
 * [AttendantFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AttendantFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AttendantFragment : Fragment(), CoroutineScope {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    @Inject
    lateinit var retrofitApiService: RetrofitApiService

    @Inject
    lateinit var job: Job

    @Inject
    lateinit var threadPool : ExecutorCoroutineDispatcher

    @Inject
    lateinit var settings : SharedPrefs

    private val attendantFragmentContext: AttendantFragment = this

    var requestOptions : RequestOptions =  RequestOptions()

    var space  = -1

    var lot = LotResponse()

    override val coroutineContext: CoroutineContext
        get() =   Dispatchers.Default + job




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)


        }

        (activity!!.application as App).applicationInjector.inject(this)
        requestOptions.centerCrop()
        requestOptions.placeholder(R.drawable.the_hub)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attendant, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setAttendant()

    }


    private fun setAttendant(){
        launch {
            withContext(threadPool) {
                lyt_progress_attendant.visibility = View.VISIBLE
                lyt_not_an_attendant.visibility = View.GONE

                retrofitApiService.getEmployeeByUserId(settings.userId!!).observe(attendantFragmentContext, Observer<ApiResponse<Employee>> { response ->
                    run {
                        if (response != null) {
                            if (response.isSuccessful) {


                                if (response.body != null) {
                                    val attendant = response.body

                                    retrofitApiService.getParkingLotById(attendant.lotId)
                                            .observe(attendantFragmentContext, Observer<ApiResponse<LotResponse>> { response ->
                                                run {
                                                    if (response != null) {
                                                        if (response.isSuccessful) {


                                                            if (response.body != null) {
                                                                val parkingLot = response.body
                                                                lot = parkingLot


                                                                retrofitApiService.getAvailableSpacesInaParkingLot(parkingLot.id).observe(
                                                                        attendantFragmentContext, Observer<ApiResponse<AvailableSpaceResponse>> { response ->
                                                                    run {
                                                                        if (response?.body != null && response.isSuccessful) {
                                                                            val availableSpaces = response.body
                                                                            attendant_updated_by.text = availableSpaces.userName
                                                                            attendant_available_spaces.text = availableSpaces.available.toString()
                                                                            attendant_used_spaces.text = (parkingLot.parkingSpaces - availableSpaces.available).toString()
                                                                            attendant_last_update_time.text = UtilityClass.getTimeDifference(availableSpaces.reportedAt)


                                                                        } else {
                                                                            attendant_available_spaces.text = parkingLot.parkingSpaces.toString()
                                                                            attendant_last_update_time.text = "Few minutes ago"
                                                                        }
                                                                    }
                                                                }
                                                                )


                                                                for (i in 0 until parkingLot.parkingLotPhotos.size) {

                                                                    val txtSliderView = DefaultSliderView(attendantFragmentContext.context)
                                                                    // if you want show image only / without description text use DefaultSliderView instead

                                                                    // initialize SliderLayout
                                                                    txtSliderView
                                                                            .image(parkingLot.parkingLotPhotos[i].blobUrl)
                                                                            .setRequestOption(requestOptions)
                                                                            .setBackgroundColor(Color.WHITE)
                                                                            .setProgressBarVisible(true)
                                                                    // .setOnSliderClickListener(true)

                                                                    //add your extra information
                                                                    txtSliderView.bundle(Bundle())
                                                                    //txtSliderView.bundle.putString("extra", name)
                                                                    slider_attendant_lot_image.addSlider(txtSliderView)
                                                                }

                                                                slider_attendant_lot_image.setPresetTransformer(SliderLayout.Transformer.Default)
                                                                slider_attendant_lot_image.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
                                                                slider_attendant_lot_image.setCustomAnimation(DescriptionAnimation())

                                                                if (parkingLot.parkingLotPhotos.isEmpty() ||parkingLot.parkingLotPhotos.size <=1){
                                                                    val txtSliderView = DefaultSliderView(attendantFragmentContext.context)
                                                                    // if you want show image only / without description text use DefaultSliderView instead

                                                                    // initialize SliderLayout
                                                                    txtSliderView
                                                                            .image(R.drawable.the_hub)
                                                                            .setRequestOption(requestOptions)
                                                                            .setBackgroundColor(Color.WHITE)
                                                                    slider_attendant_lot_image.removeAllSliders()
                                                                    slider_attendant_lot_image.addSlider(txtSliderView)
                                                                    slider_attendant_lot_image.stopAutoCycle()
                                                                }else{
                                                                    slider_attendant_lot_image.setDuration(  ((6000 until 8000).random() ).toLong() )
                                                                }




                                                                attendant_total_spaces.text = parkingLot.parkingSpaces.toString()
                                                                attendant_lot_name.text = parkingLot.name
                                                                Glide.with(attendantFragmentContext).load(FirebaseAuth.getInstance().currentUser!!.photoUrl).into(attendant_avatar)

                                                                lyt_progress_attendant.visibility = View.GONE
                                                                attendant_lyt.visibility = View.VISIBLE

                                                                attendant_refresh_btn.setOnClickListener{
                                                                    refreshSpaces()
                                                                }

                                                                attendant_update_btn.setOnClickListener{
                                                                    updateAvailableSpace()
                                                                }


                                                            } else {
                                                                notAnAttendant()
                                                            }
                                                        } else {
                                                            notAnAttendant()
                                                        }
                                                    }
                                                }
                                            }
                                            )


                                }
                            } else {
                                notAnAttendant()
                            }
                        } else {
                            notAnAttendant()
                        }
                    }
                }
                )
                if(attendant_lyt.visibility!=View.VISIBLE){
                    lyt_progress_attendant.visibility = View.GONE
                    lyt_not_an_attendant.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateAvailableSpace(){
        showCreateDialog()

    }
    private fun showCreateDialog() {
        val context = this
        val builder = AlertDialog.Builder(this.context!!)
        builder.setTitle("Update")

        val view = layoutInflater.inflate(R.layout.attendant_update_layout, null)


        builder.setView(view);

        // set up the ok button
        builder.setPositiveButton("UPDATE") { dialog, p1 ->
            space =  view.edSpaces.text.toString().toInt()
            val isValid: Boolean
            if (space == -1 && lot.id == "") {
                lyt_space.error = "Enter spaces available!"
                isValid = false
            } else {
                isValid = true
            }

            if (isValid) {
                // do something
                val availableSpaceUpdate = AvailableSpaceUpdate()
                availableSpaceUpdate.available = space
                availableSpaceUpdate.lotId = lot.id

                launch {
                    withContext(threadPool) {
                        retrofitApiService.updateAvailableSpaces(availableSpaceUpdate)
                                .observe(attendantFragmentContext,
                                        Observer<ApiResponse<AvailableSpaceResponse>> { response ->
                                            run {
                                                if (response?.body != null && response.isSuccessful) {
                                                    val availableSpaces = response.body
                                                    attendant_updated_by.text = availableSpaces.userName
                                                    attendant_used_spaces.text  = (lot.parkingSpaces - availableSpaces.available).toString()
                                                    attendant_available_spaces.text = availableSpaces.available.toString()
                                                    attendant_last_update_time.text = UtilityClass.getTimeDifference(availableSpaces.reportedAt)

                                                }
                                            }
                                        })
                    }


                }

                if (isValid) {
                    dialog.dismiss()
                }
            }


        }

        builder.setNegativeButton(android.R.string.cancel) { dialog, p1 ->
            dialog.cancel()
        }

        builder.show()
    }

    private  fun refreshSpaces(){

        launch {
            withContext(threadPool) {
                retrofitApiService.getEmployeeByUserId(settings.userId!!).observe(attendantFragmentContext, Observer<ApiResponse<Employee>> { response ->
                    run {
                        if (response != null) {
                            if (response.isSuccessful) {


                                if (response.body != null) {
                                    val attendant = response.body

                                    retrofitApiService.getParkingLotById(attendant.lotId)
                                            .observe(attendantFragmentContext, Observer<ApiResponse<LotResponse>> { response ->
                                                run {
                                                    if (response != null) {
                                                        if (response.isSuccessful) {


                                                            if (response.body != null) {
                                                                val parkingLot = response.body


                                                                retrofitApiService.getAvailableSpacesInaParkingLot(parkingLot.id).observe(
                                                                        attendantFragmentContext, Observer<ApiResponse<AvailableSpaceResponse>> { response ->
                                                                    run {
                                                                        if (response?.body != null && response.isSuccessful) {
                                                                            val availableSpaces = response.body
                                                                            attendant_available_spaces.text = availableSpaces.available.toString()
                                                                            attendant_used_spaces.text = (parkingLot.parkingSpaces - availableSpaces.available).toString()
                                                                            attendant_last_update_time.text = UtilityClass.getTimeDifference(availableSpaces.reportedAt)


                                                                        } else {
                                                                            attendant_available_spaces.text = parkingLot.parkingSpaces.toString()
                                                                            attendant_last_update_time.text = "Few minutes ago"
                                                                        }
                                                                    }
                                                                }
                                                                )


                                                            }



                                                        }
                                                    }
                                                }
                                            }
                                            )


                                }
                            }
                        }
                    }
                }
                )
            }
        }
    }

    private fun notAnAttendant(){
        lyt_progress_attendant.visibility = View.GONE
        lyt_not_an_attendant.visibility = View.VISIBLE
    }
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
         * @return A new instance of fragment AttendantFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                AttendantFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
    private fun IntRange.random() =
            Random().nextInt((endInclusive + 1) - start) +  start
}
