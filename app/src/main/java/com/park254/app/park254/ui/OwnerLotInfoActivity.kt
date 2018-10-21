package com.park254.app.park254.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.request.RequestOptions
import com.glide.slider.library.Animations.DescriptionAnimation
import com.glide.slider.library.SliderLayout
import com.glide.slider.library.SliderTypes.DefaultSliderView
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.ui.repo.HomeViewModel
import kotlinx.android.synthetic.main.activity_owner_lot_info.*
import java.util.*
import javax.inject.Inject

class OwnerLotInfoActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModel: HomeViewModel


    var requestOptions : RequestOptions =  RequestOptions()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_lot_info)
        (application as App).applicationInjector.inject(this)
        initToolbar()

        requestOptions.centerCrop()
        requestOptions.placeholder(R.drawable.parking_lot_image_preview)
        if(viewModel.parsedLot !=null){
            owner_lot_info_name.text = viewModel.parsedLot!!.name
            owner_lot_info_street.text = viewModel.parsedLot!!.streetName



            for (i in 0 until viewModel.parsedLot!!.parkingLotPhotos.size) {

                val txtSliderView = DefaultSliderView(this)
                // if you want show image only / without description text use DefaultSliderView instead

                // initialize SliderLayout
                txtSliderView
                        .image(viewModel.parsedLot!!.parkingLotPhotos[i].blobUrl)
                        .setRequestOption(requestOptions)
                        .setBackgroundColor(Color.WHITE)
                        .setProgressBarVisible(true)
                // .setOnSliderClickListener(true)

                //add your extra information
                txtSliderView.bundle(Bundle())
                //txtSliderView.bundle.putString("extra", name)
                owner_lot_header_slider_image.addSlider(txtSliderView)
            }

            owner_lot_header_slider_image.setPresetTransformer(SliderLayout.Transformer.Default)
            owner_lot_header_slider_image.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
            owner_lot_header_slider_image.setCustomAnimation(DescriptionAnimation())

            if (viewModel.parsedLot!!.parkingLotPhotos.isEmpty() || viewModel.parsedLot!!.parkingLotPhotos.size <=1){
                val txtSliderView = DefaultSliderView(this)
                // if you want show image only / without description text use DefaultSliderView instead

                // initialize SliderLayout
                txtSliderView
                        .image(R.drawable.parking_lot_image_preview)
                        .setRequestOption(requestOptions)
                        .setBackgroundColor(Color.WHITE)
                owner_lot_header_slider_image.removeAllSliders()
                owner_lot_header_slider_image.addSlider(txtSliderView)
                owner_lot_header_slider_image.stopAutoCycle()
            }else{
                owner_lot_header_slider_image.setDuration(  ((6000 until 8000).random() ).toLong() )
            }
        }

        fab_update_info.setOnClickListener{
            startActivity(Intent(this@OwnerLotInfoActivity, UpdateInfoActivity::class.java))
        }

        fab_employees.setOnClickListener{
            startActivity(Intent(this@OwnerLotInfoActivity, EmployeeActivity::class.java))
        }
        fab_delete_lot.setOnClickListener{

        }

    }
    private fun initToolbar() {
        toolbar2.setNavigationIcon(R.drawable.ic_back_arrow)
        setSupportActionBar(toolbar2)
        supportActionBar!!.title = "Parking Lot"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar2.setNavigationOnClickListener {
            finish()
        }
    }

    fun IntRange.random() =
            Random().nextInt((endInclusive + 1) - start) +  start
}
