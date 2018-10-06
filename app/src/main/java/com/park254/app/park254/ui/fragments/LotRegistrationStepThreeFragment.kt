package com.park254.app.park254.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.park254.app.park254.R
import com.park254.app.park254.ui.ParkingLotRegistrationActivity
import com.park254.app.park254.utils.UtilityClass
import kotlinx.android.synthetic.main.fragment_lot_registration_step_three.*
import java.io.File


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LotRegistrationStepThreeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LotRegistrationStepThreeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class LotRegistrationStepThreeFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_lot_registration_step_three, container, false)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        input_btn_add_img_one.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, UtilityClass.OPEN_DOCUMENT_FIRST_PHOTO_CODE)
        }

        input_btn_add_img_two.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, UtilityClass.OPEN_DOCUMENT_SECOND_PHOTO_CODE)
        }

        input_btn_add_img_three.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, UtilityClass.OPEN_DOCUMENT_THIRD_PHOTO_CODE)
        }

        input_btn_clear_img_one.setOnClickListener{
            clearImageInfo(input_img_upload_one_txt_view, display_img_upload_one)
            input_btn_clear_img_one.visibility = View.GONE
            (activity as ParkingLotRegistrationActivity).viewModel.imageTwoUri = null
        }

        input_btn_clear_img_two.setOnClickListener{
            clearImageInfo(input_img_upload_two_txt_view, display_img_upload_two)
            input_btn_clear_img_two.visibility = View.GONE
            (activity as ParkingLotRegistrationActivity).viewModel.imageTwoUri = null
        }

        input_btn_clear_img_three.setOnClickListener{
            clearImageInfo(input_img_upload_three_txt_view, display_img_upload_three)
            input_btn_clear_img_three.visibility = View.GONE
            (activity as ParkingLotRegistrationActivity).viewModel.imageThreeUri = null
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ( resultCode == RESULT_OK) {

            if (requestCode == UtilityClass.OPEN_DOCUMENT_FIRST_PHOTO_CODE) {
                if (data != null) {
                    // this is the image selected by the user
                    (activity as ParkingLotRegistrationActivity).viewModel.imageOneUri = data.data

                }
            }

            else if (requestCode == UtilityClass.OPEN_DOCUMENT_SECOND_PHOTO_CODE) {
                if (data != null) {
                    // this is the image selected by the user
                    (activity as ParkingLotRegistrationActivity).viewModel.imageTwoUri = data.data

                }
            }

            else if (requestCode == UtilityClass.OPEN_DOCUMENT_THIRD_PHOTO_CODE  ) {
                if (data != null) {
                    // this is the image selected by the user
                    (activity as ParkingLotRegistrationActivity).viewModel.imageThreeUri = data.data

                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        val imageOneUri = (activity as ParkingLotRegistrationActivity).viewModel.imageOneUri
        if(imageOneUri!= null){

            displayImageInfo(imageOneUri,input_img_upload_one_txt_view, input_btn_clear_img_one, display_img_upload_one,1)
        }

        val imageTwoUri = (activity as ParkingLotRegistrationActivity).viewModel.imageTwoUri
        if(imageTwoUri!= null){

        displayImageInfo(imageTwoUri,input_img_upload_two_txt_view,input_btn_clear_img_two, display_img_upload_two,2)


        }

        val imageThreeUri = (activity as ParkingLotRegistrationActivity).viewModel.imageThreeUri
        if(imageThreeUri!= null){

            displayImageInfo(imageThreeUri,input_img_upload_three_txt_view,input_btn_clear_img_three,display_img_upload_three,3)


        }


    }

    fun displayImageInfo(uri: Uri, txtView:TextView, btnClear:ImageView, displayView: ImageView, number:Int){
        Glide.with((activity as ParkingLotRegistrationActivity))
                .load(uri)
                .thumbnail(0.1f)
                .into(displayView)

        val myFile = File(uri.toString())
        val path = myFile.absolutePath
        var displayName: String? = null

        if (uri.toString().startsWith("content://")) {
            var cursor: Cursor? = null
            try {
                cursor = activity!!.contentResolver.query(uri, null, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    when(number){
                        1->(activity as ParkingLotRegistrationActivity).viewModel.imageOneLabel =displayName
                        2->(activity as ParkingLotRegistrationActivity).viewModel.imageTwoLabel =displayName
                        3->(activity as ParkingLotRegistrationActivity).viewModel.imageThreeLabel =displayName
                    }
                }
            } finally {
                cursor!!.close()
            }
        } else if (uri.toString().startsWith("file://")) {
            displayName = myFile.name
        }
        if (displayName !=null){
            txtView.text = displayName
            btnClear.visibility = View.VISIBLE
        }

    }

    fun clearImageInfo(txtView:TextView, imageView: ImageView){
        txtView.text  = "Select Image"
        imageView.setImageResource(android.R.color.transparent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LotRegistrationStepThreeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                LotRegistrationStepThreeFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
