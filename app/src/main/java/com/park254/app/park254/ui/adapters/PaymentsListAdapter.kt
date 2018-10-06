package com.park254.app.park254.ui.adapters

import android.arch.lifecycle.Observer
import android.content.Context
import android.icu.util.Calendar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.models.Booking
import com.park254.app.park254.models.LotResponse
import com.park254.app.park254.models.Payment
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.ui.PaymentsActivity
import com.park254.app.park254.utils.ItemAnimation
import com.park254.app.park254.utils.UtilityClass
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import kotlinx.android.synthetic.main.payment_item.view.*
import java.io.FileNotFoundException

import javax.inject.Inject

class PaymentsListAdapter(private val ctx: Context, items: ArrayList<Payment>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    @Inject
    lateinit var retrofitApiService: RetrofitApiService



    private var items = ArrayList<Payment>()

    private var mOnItemClickListener: OnItemClickListener? = null



    private var lastPosition = -1
    private var on_attach = true

    var onItemClick: ((Payment) -> Unit)? = null



    interface OnItemClickListener {
        fun onItemClick(view: View, obj: Payment, position: Int)
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
        var booking_amount = v.booking_amount
        var check_in_date = v.check_in_date
        var check_in_time = v.check_in_time
        var check_out_date = v.check_out_date
        var check_out_time = v.check_out_time
        var paid_on = v.paid_on_txtview


        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(items[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(R.layout.payment_item, parent, false)
        vh = OriginalViewHolder(v)
        return vh
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is OriginalViewHolder) {
            val paymentItem = items[position]

           val paidDate = UtilityClass.getDateWithServerTimeStamp(paymentItem.receivedOn)

           holder.paid_on.text = Calendar.DATE.toString()+  " " + UtilityClass.getMonthForInt(paidDate?.get(Calendar.MONTH)!!).substring(0,3)  +" " + paidDate.get(Calendar.YEAR).toString()


            retrofitApiService.geBookingById(paymentItem.paymentReference).observe(ctx as PaymentsActivity, Observer<ApiResponse<Booking>>{
                response->run{
                if (response?.body !=null && response.isSuccessful){

                    val bookedItem = response.body



                        // val bookingDate = UtilityClass.getDateWithServerTimeStamp(bookedItem.bookedOn)

                        holder.booking_amount.text = "KES. " + bookedItem.cost
                        //Calendar.DATE.toString()+  " " + UtilityClass.getMonthForInt(bookingDate?.get(Calendar.MONTH)!!).substring(0,3)  +" " + bookingDate?.get(Calendar.YEAR).toString()

                        val checkIn = UtilityClass.getDateWithServerTimeStamp(bookedItem.starting)
                        holder.check_in_date.text = checkIn?.get(Calendar.DATE).toString()+ " " + UtilityClass.getMonthForInt(checkIn?.get(Calendar.MONTH)!!)
                        holder.check_in_time.text = checkIn.get(Calendar.HOUR).toString()+ " : " + UtilityClass.addZeroForOneToNine(checkIn.get(Calendar.MINUTE) )+ " " + UtilityClass.timeAMorPM(checkIn)

                        val checkOut = UtilityClass.getDateWithServerTimeStamp(bookedItem.ending)
                        holder.check_out_date.text = checkOut?.get(Calendar.DATE).toString()+ " " + UtilityClass.getMonthForInt(checkOut?.get(Calendar.MONTH)!!)
                        holder.check_out_time.text = checkOut.get(Calendar.HOUR).toString()+ ":" + UtilityClass.addZeroForOneToNine(checkIn.get(Calendar.MINUTE)) +  " " + UtilityClass.timeAMorPM(checkOut)



                }
            }
            })





            retrofitApiService.getParkingLotById(paymentItem.lotId).observe(
                    ctx, Observer<ApiResponse<LotResponse>> {
                response ->run{
                if (response?.body != null && response.isSuccessful) {
                    if(response.body.parkingLotPhotos.isNotEmpty()) {
                        try {
                            Glide.with(ctx as PaymentsActivity).load(response.body.parkingLotPhotos[0].blobUrl).into(holder.imageView)
                        } catch (e: FileNotFoundException) {

                        }
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