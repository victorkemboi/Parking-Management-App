package com.park254.app.park254.ui.adapters

import android.arch.lifecycle.Observer
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.models.AvailableSpaceResponse
import com.park254.app.park254.models.Lot
import com.park254.app.park254.models.LotResponse
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.ui.HomeActivity
import com.park254.app.park254.utils.ItemAnimation
import com.park254.app.park254.utils.UtilityClass
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import kotlinx.android.synthetic.main.owned_lot_item.view.*
import java.util.ArrayList
import javax.inject.Inject

class OwnerListAdapter(private val ctx: Context, items: ArrayList<LotResponse>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = ArrayList<LotResponse>()
    private var mOnItemClickListener: OnItemClickListener? = null

    private var lastPosition = -1
    private var on_attach = true

    var onItemClick: ((LotResponse) -> Unit)? = null

    @Inject
    lateinit var retrofitApiService: RetrofitApiService

    interface OnItemClickListener {
        fun onItemClick(view: View, obj: LotResponse, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mItemClickListener
    }

    init {
        this.items = items
        (ctx.applicationContext as App).applicationInjector.inject(this)
    }

    inner class OriginalViewHolder(v: View) : RecyclerView.ViewHolder(v) {



        var image = v.card_lot_item_image
        var name = v.parking_lot_name
        var lyt_parent = v.lin_lyt_parent_item as View
        var street_name = v.lot_item_street_name
        var emp_available = v.emp_available
        var emp_total = v.emp_total
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(items[adapterPosition])
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(R.layout.owned_lot_item, parent, false)
        vh = OriginalViewHolder(v)
        return vh
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.e("onBindViewHolder", "onBindViewHolder : $position")
        if (holder is OriginalViewHolder) {


            val parkingLotItem = items[position]

            retrofitApiService.getAvailableSpacesInaParkingLot(parkingLotItem.id).observe(
                    ctx as HomeActivity, Observer<ApiResponse<AvailableSpaceResponse>> {
                response ->run{
                if (response?.body != null && response.isSuccessful) {
                    holder.emp_available.text =response.body.available.toString()


                }else{
                    holder.emp_available.text =parkingLotItem.parkingSpaces.toString()
                }
            }
            }
            )
            holder.emp_total.text = parkingLotItem.parkingSpaces.toString()
            holder.name.text = parkingLotItem.name
            holder.street_name.text = parkingLotItem.streetName
            holder.lyt_parent.setOnClickListener { view ->
                if (mOnItemClickListener != null) {
                    mOnItemClickListener!!.onItemClick(view, items[position], position)
                }
            }

            if(parkingLotItem.parkingLotPhotos.isNotEmpty()){
                Glide.with(ctx).load(parkingLotItem.parkingLotPhotos[0].blobUrl).into(holder.image)
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