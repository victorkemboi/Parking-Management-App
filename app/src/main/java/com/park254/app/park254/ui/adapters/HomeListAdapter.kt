package com.park254.app.park254.ui.adapters

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.park254.app.park254.R
import com.park254.app.park254.models.LotResponse
import com.park254.app.park254.utils.ItemAnimation
import kotlinx.android.synthetic.main.card_item_parking_lot_info.view.*
import android.os.Bundle
import com.glide.slider.library.SliderLayout
import com.glide.slider.library.SliderTypes.DefaultSliderView
import com.glide.slider.library.Animations.DescriptionAnimation
import com.glide.slider.library.Tricks.ViewPagerEx
import com.park254.app.park254.App
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.utils.UtilityClass
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import java.util.*
import javax.inject.Inject

import android.arch.lifecycle.Observer
import com.park254.app.park254.models.AvailableSpaceResponse
import com.park254.app.park254.ui.HomeActivity


class HomeListAdapter(private val ctx: Context, items: ArrayList<LotResponse>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ViewPagerEx.OnPageChangeListener {
    @Inject
    lateinit var retrofitApiService: RetrofitApiService

    override fun onPageScrollStateChanged(p0: Int) {


    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

        //Log.d("Page scrolled", p0.toString()+ " : " + p1.toString() + " : " + p2 .toString())
    }



    override fun onPageSelected(p0: Int) {

    }

    private var items = ArrayList<LotResponse>()
    private var mOnItemClickListener: OnItemClickListener? = null

    var requestOptions : RequestOptions =  RequestOptions()



    private var lastPosition = -1
    private var on_attach = true

    var onItemClick: ((LotResponse) -> Unit)? = null



    interface OnItemClickListener {
        fun onItemClick(view: View, obj: LotResponse, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mItemClickListener
    }

    init {
        (ctx.applicationContext as App).applicationInjector.inject(this)
        this.items = items
        requestOptions.centerCrop()
        requestOptions.placeholder(R.drawable.parking_lot_image_preview)


    }

    inner class OriginalViewHolder(v: View) : RecyclerView.ViewHolder(v) {
      var imageSlider = v.slider as SliderLayout
        var name = v.parking_name
        var lyt_parent = v.lin_lyt_parent as View
        var street_name = v.street_name
        var spots_available = v.spots_available
        var time_updated = v.time_updated

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(items[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_item_parking_lot_info, parent, false)
        vh = OriginalViewHolder(v)
        return vh
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.e("onBindViewHolder", "onBindViewHolder : $position")
        if (holder is OriginalViewHolder) {

            val p = items[position]

            retrofitApiService.getAvailableSpacesInaParkingLot(p.id).observe(
                    ctx as HomeActivity,Observer<ApiResponse<AvailableSpaceResponse>> {
                response ->run{
                if (response?.body != null && response.isSuccessful) {
                    holder.spots_available.text =response.body.available.toString() + " Spots"

                   holder.time_updated.text = UtilityClass.getTimeDifference(response.body.reportedAt)

                }else{
                    holder.spots_available.text =p.parkingSpaces.toString() + " Spots"
                    holder.time_updated.text = "FEW MINUTES AGO"
                }
            }
            }
            )




            for (i in 0 until p.parkingLotPhotos.size) {

                val txtSliderView = DefaultSliderView(ctx)
                // if you want show image only / without description text use DefaultSliderView instead

                // initialize SliderLayout
                txtSliderView
                        .image(p.parkingLotPhotos[i].blobUrl)
                        .setRequestOption(requestOptions)
                        .setBackgroundColor(Color.WHITE)
                        .setProgressBarVisible(true)
                // .setOnSliderClickListener(true)

                //add your extra information
                txtSliderView.bundle(Bundle())
                //txtSliderView.bundle.putString("extra", name)
                holder.imageSlider.addSlider(txtSliderView)
            }

            holder.imageSlider.setPresetTransformer(SliderLayout.Transformer.Default)
            holder.imageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
            holder.imageSlider.setCustomAnimation(DescriptionAnimation())

            if (p.parkingLotPhotos.isEmpty() || p.parkingLotPhotos.size <=1){
                val txtSliderView = DefaultSliderView(ctx)
                // if you want show image only / without description text use DefaultSliderView instead

                // initialize SliderLayout
                txtSliderView
                        .image(R.drawable.parking_lot_image_preview)
                        .setRequestOption(requestOptions)
                        .setBackgroundColor(Color.WHITE)
                holder.imageSlider.removeAllSliders()
                holder.imageSlider.addSlider(txtSliderView)
                holder.imageSlider.stopAutoCycle()
            }else{
                holder.imageSlider.setDuration(  ((6000 until 8000).random() ).toLong() )
            }


            holder.name.text = p.name
            holder.street_name.text = p.streetName

            holder.lyt_parent.setOnClickListener { view ->
                if (mOnItemClickListener != null) {
                    mOnItemClickListener!!.onItemClick(view, items[position], position)
                }
            }


            setAnimation(holder.itemView, position)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                on_attach = false
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun setAnimation(view: View, position: Int) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, if (on_attach) position else -1, 2)
            lastPosition = position
        }
    }





} fun IntRange.random() =
        Random().nextInt((endInclusive + 1) - start) +  start