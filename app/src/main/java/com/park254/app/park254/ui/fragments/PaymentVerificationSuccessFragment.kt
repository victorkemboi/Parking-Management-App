package com.park254.app.park254.ui.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import com.park254.app.park254.App

import com.park254.app.park254.R
import com.park254.app.park254.ui.repo.PaymentVerificationViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.payment_verification_dialog.*
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PaymentVerificationSuccessFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PaymentVerificationSuccessFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PaymentVerificationSuccessFragment : DialogFragment() {

    @Inject
    lateinit var paymentVerificationViewModel: PaymentVerificationViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

       val  rootView = inflater.inflate(R.layout.payment_verification_dialog, container, false)

        rootView.findViewById<FloatingActionButton>(R.id.fab_close_verification_dialog) .setOnClickListener {
           activity!!.finish()

        }

        rootView.findViewById<TextView>(R.id.payment_dialog_amount).text = paymentVerificationViewModel.payment?.amount
        rootView.findViewById<TextView>(R.id.payment_dialog_phone_number).text =  paymentVerificationViewModel.payment?.paidBy

        return rootView
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }


}
