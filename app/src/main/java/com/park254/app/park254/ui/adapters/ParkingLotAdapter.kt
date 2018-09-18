package com.park254.app.park254.ui.adapters

import android.content.Context
import android.widget.TextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.park254.app.park254.R
import com.park254.app.park254.models.Lot
import com.park254.app.park254.models.Rate
import kotlinx.android.synthetic.main.card_item_parking_lot_info.view.*
import kotlinx.android.synthetic.main.parking_rates_layout.view.*


class ParkingLotAdapter (val items : ArrayList<Rate>, val context: Context): RecyclerView.Adapter<ParkingLotAdapter.ViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ParkingLotAdapter.ViewHolder {

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.parking_rates_layout, parent, false))
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.



    }

    override fun getItemCount(): Int {
        return items.size
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.min_time.text = items[position].minimumTime.toString()
        holder.max_time.text = items[position].maximumTime.toString()
        holder.cost.text = items[position].cost.toString()

    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each parking lot
        val min_time = view.parking_rate_min_time
        val max_time = view.parking_rate_max_time
        val cost = view.parking_rate_cost
    }



}