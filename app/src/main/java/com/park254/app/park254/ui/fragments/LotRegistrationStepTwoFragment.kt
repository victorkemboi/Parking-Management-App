package com.park254.app.park254.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.park254.app.park254.R
import com.park254.app.park254.ui.ParkingLotRegistrationActivity
import kotlinx.android.synthetic.main.activity_parking_lot_registration.*
import kotlinx.android.synthetic.main.fragment_lot_registration_step_two.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LotRegistrationStepTwoFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LotRegistrationStepTwoFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class LotRegistrationStepTwoFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_lot_registration_step_two, container, false)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        input_max_time.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                input_min_time_two.text = input_max_time.text
                input_min_time_five.text = input_max_time.text
            }
        })


        input_max_time_two.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                input_min_time_three.text = input_max_time_two.text

                input_min_time_five.text = input_max_time_two.text
            }
        })

        input_max_time_three.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                input_min_time_four.text = input_max_time_three.text

                input_min_time_five.text = input_max_time_three.text
            }
        })

        input_max_time_four.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                input_min_time_five.text = input_max_time_four.text
            }
        })


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
         * @return A new instance of fragment LotRegistrationStepTwoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                LotRegistrationStepTwoFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onResume() {
        super.onResume()
        fillViewValues()
    }

    private fun fillViewValues() {

        //first rate

        if ((activity as ParkingLotRegistrationActivity).viewModel.rate1.minimumTime != 0) {
            input_min_time.setText((activity as ParkingLotRegistrationActivity).viewModel.rate1.minimumTime.toString())
        }
        if ((activity as ParkingLotRegistrationActivity).viewModel.rate1.maximumTime != 0) {
            input_max_time.setText((activity as ParkingLotRegistrationActivity).viewModel.rate1.maximumTime.toString())
        }
        if ((activity as ParkingLotRegistrationActivity).viewModel.rate1.cost != 0.0) {
            input_cost.setText((activity as ParkingLotRegistrationActivity).viewModel.rate1.cost.toString())
        }

        //second rate
        if ((activity as ParkingLotRegistrationActivity).viewModel.rate2.minimumTime != 0) {
            input_min_time_two.setText((activity as ParkingLotRegistrationActivity).viewModel.rate2.minimumTime.toString())
        }
        if ((activity as ParkingLotRegistrationActivity).viewModel.rate2.maximumTime != 0) {
            input_max_time_two.setText((activity as ParkingLotRegistrationActivity).viewModel.rate2.maximumTime.toString())
        }
        if ((activity as ParkingLotRegistrationActivity).viewModel.rate2.cost != 0.0) {
            input_cost_two.setText((activity as ParkingLotRegistrationActivity).viewModel.rate2.cost.toString())
        }

        //third rate
        if ((activity as ParkingLotRegistrationActivity).viewModel.rate3.minimumTime != 0) {
            input_min_time_three.setText((activity as ParkingLotRegistrationActivity).viewModel.rate3.minimumTime.toString())
        }
        if ((activity as ParkingLotRegistrationActivity).viewModel.rate3.maximumTime != 0) {
            input_max_time_three.setText((activity as ParkingLotRegistrationActivity).viewModel.rate3.maximumTime.toString())
        }
        if ((activity as ParkingLotRegistrationActivity).viewModel.rate3.cost != 0.0) {
            input_cost_three.setText((activity as ParkingLotRegistrationActivity).viewModel.rate3.cost.toString())
        }

        //fourth rate
        if ((activity as ParkingLotRegistrationActivity).viewModel.rate4.minimumTime != 0) {
            input_min_time_four.setText((activity as ParkingLotRegistrationActivity).viewModel.rate4.minimumTime.toString())
        }
        if ((activity as ParkingLotRegistrationActivity).viewModel.rate4.maximumTime != 0) {
            input_max_time_four.setText((activity as ParkingLotRegistrationActivity).viewModel.rate4.maximumTime.toString())
        }
        if ((activity as ParkingLotRegistrationActivity).viewModel.rate4.cost != 0.0) {
            input_cost_four.setText((activity as ParkingLotRegistrationActivity).viewModel.rate4.cost.toString())
        }

        //fifth rate
        if ((activity as ParkingLotRegistrationActivity).viewModel.rate5.cost != 0.0) {
            input_cost_five.setText((activity as ParkingLotRegistrationActivity).viewModel.rate5.cost.toString())
        }


    }

}
