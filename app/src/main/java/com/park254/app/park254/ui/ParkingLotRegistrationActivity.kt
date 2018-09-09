package com.park254.app.park254.ui

import android.graphics.PorterDuff
import android.opengl.Visibility
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.ui.repo.ParkingLotRegistrationViewModel
import com.park254.app.park254.utils.ViewAnimation
import kotlinx.android.synthetic.main.activity_parking_lot_registration.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class ParkingLotRegistrationActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: ParkingLotRegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_lot_registration)

        App.applicationInjector.inject(this)
        initToolbar()
        initComponent()

        lyt_back.visibility = View.INVISIBLE

    }


    private fun initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Registration"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }


    private fun initComponent() {

        lyt_back.setOnClickListener {
            backStep(viewModel.current_step)
            bottomProgressDots(viewModel.current_step)
        }

        lyt_next.setOnClickListener {
            nextStep(viewModel.current_step)
            bottomProgressDots(viewModel.current_step)
        }

        bottomProgressDots(viewModel.current_step)
    }

    private fun nextStep(progressActive: Int) {
        var progress = progressActive
        if (progress < viewModel.MAX_STEP) {
            progress++
            viewModel.current_step = progress
            if (progress>1){
                lyt_back.visibility = View.VISIBLE
            }else{
                lyt_back.visibility = View.INVISIBLE
            }
            if (progress == viewModel.MAX_STEP){
                lyt_next_txt_view.text = "FINISH"
            }
          //  ViewAnimation.fadeOutIn(status)
        }
    }

    private fun backStep(progressActive: Int) {
        var progress = progressActive

        if (progress > 1) {
            if (progress == viewModel.MAX_STEP){
                lyt_next_txt_view.text = "NEXT"
            }
            progress--
            viewModel.current_step = progress
            if (progress>1){

                lyt_back.visibility = View.VISIBLE
            }else if(progress == 1){
                lyt_back.visibility = View.INVISIBLE
            }
            //ViewAnimation.fadeOutIn(status)
        }
    }

    private fun bottomProgressDots(current_active_index: Int) {
        var current_index = current_active_index
        current_index--
        val dotsLayout = findViewById<LinearLayout>(R.id.layoutDots)
        val dots = arrayOfNulls<ImageView>(viewModel.MAX_STEP)

        dotsLayout.removeAllViews()
        for (i in dots.indices) {
            dots[i] = ImageView(this)
            val widthHeight = 15
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams(widthHeight, widthHeight))
            params.setMargins(10, 10, 10, 10)
            dots[i]!!.layoutParams = params
            dots[i]!!.setImageResource(R.drawable.shape_circle)
            dots[i]!!.setColorFilter(resources.getColor(R.color.grey_20), PorterDuff.Mode.SRC_IN)
            dotsLayout.addView(dots[i])
        }

        if (dots.isNotEmpty()) {
            dots[current_index]!!.setImageResource(R.drawable.shape_circle)
            dots[current_index]!!.setColorFilter(resources.getColor(R.color.colorAccentDark2), PorterDuff.Mode.SRC_IN)
        }
    }
}
