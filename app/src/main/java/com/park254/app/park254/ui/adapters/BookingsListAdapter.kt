package com.park254.app.park254.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.models.Booking
import com.park254.app.park254.models.LotResponse
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.ui.BookingsActivity
import com.park254.app.park254.utils.ItemAnimation
import kotlinx.android.synthetic.main.booked_lot_item.view.*
import java.util.*
import javax.inject.Inject

import android.arch.lifecycle.Observer
import com.bumptech.glide.load.engine.GlideException
import com.park254.app.park254.utils.UtilityClass
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import java.io.FileNotFoundException


class BookingsListAdapter(private val ctx: Context, items: ArrayList<Booking>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    @Inject
    lateinit var retrofitApiService: RetrofitApiService



    private var items = ArrayList<Booking>()
    private var mOnItemClickListener: OnItemClickListener? = null



    private var lastPosition = -1
    private var on_attach = true

    var onItemClick: ((Booking) -> Unit)? = null



    interface OnItemClickListener {
        fun onItemClick(view: View, obj: Booking, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mItemClickListener
    }

    init {
        this.items = items

        (ctx.applicationContext as App).applicationInjector.inject(this)
    }

    inner class OriginalViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var imageView = v.booking_lot_image
        var name = v.lot_name
        var lyt_parent = v.lyt_parent_booked_item as View
        var booking_date = v.booking_date
        var check_in_date = v.check_in_date
        var check_in_time = v.check_in_time
        var check_out_date = v.check_out_date
        var check_out_time = v.check_out_time


        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(items[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(R.layout.booked_lot_item, parent, false)
        vh = OriginalViewHolder(v)
        return vh
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.e("onBindViewHolder", "onBindViewHolder : $position")
        if (holder is OriginalViewHolder) {

            val bookedItem = items[position]


            val bookingDate = UtilityClass.getDateWithServerTimeStamp(bookedItem.bookedOn)
            holder.booking_date.text = UtilityClass.getMonthForInt(bookingDate?.get(Calendar.MONTH)!!)  +" " + bookingDate?.get(Calendar.YEAR).toString()

            val checkIn = UtilityClass.getDateWithServerTimeStamp(bookedItem.starting)
            holder.check_in_date.text = checkIn?.get(Calendar.DATE).toString()+ " " + UtilityClass.getMonthForInt(checkIn?.get(Calendar.MONTH)!!)
            holder.check_in_time.text = checkIn?.get(Calendar.HOUR).toString()+ " : " + Calendar.MINUTE!!

            val checkOut = UtilityClass.getDateWithServerTimeStamp(bookedItem.starting)
            holder.check_out_date.text = checkOut?.get(Calendar.DATE).toString()+ " " + UtilityClass.getMonthForInt(checkOut?.get(Calendar.MONTH)!!)
            holder.check_out_time.text = checkOut?.get(Calendar.HOUR).toString()+ " : " + Calendar.MINUTE!!

           retrofitApiService.getParkingLotById(bookedItem.parkingLotId).observe(
                    ctx as BookingsActivity,Observer<ApiResponse<LotResponse>> {
                response ->run{
                if (response?.body != null && response.isSuccessful) {
                    try {
                        Glide.with(ctx).load(response.body.parkingLotPhotos[0].blobUrl).into(holder.imageView)
                    }catch ( e: FileNotFoundException){

                    }

                }
            }
            }
            )

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


}