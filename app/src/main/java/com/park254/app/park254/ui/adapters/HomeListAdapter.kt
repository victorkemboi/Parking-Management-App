package com.park254.app.park254.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.park254.app.park254.R
import com.park254.app.park254.models.Lot
import com.park254.app.park254.models.LotResponse
import com.park254.app.park254.utils.ItemAnimation
import kotlinx.android.synthetic.main.card_item_parking_lot_info.view.*
import java.util.ArrayList



class HomeListAdapter(private val ctx: Context, items: ArrayList<LotResponse>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = ArrayList<LotResponse>()
    private var mOnItemClickListener: OnItemClickListener? = null

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
        this.items = items
    }

    inner class OriginalViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var image = v.card_parking_item_image
        var name = v.parking_name
        var lyt_parent = v.lin_lyt_parent as View
        var street_name = v.street_name
        var spots_available = v.spots_available

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
            holder.name.text = p.name
            holder.street_name.text = p.streetName
            holder.spots_available.text ="Spots: " + p.parkingSpaces.toString()
            //add glide to display image



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