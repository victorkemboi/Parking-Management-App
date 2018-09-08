package com.park254.app.park254.ui.adapters

import android.content.Context
import android.widget.TextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.park254.app.park254.R
import com.park254.app.park254.models.Lot
import kotlinx.android.synthetic.main.card_item_parking_lot_info.view.*


class ParkingLotAdapter (val items : ArrayList<Lot>, val context: Context): RecyclerView.Adapter<ParkingLotAdapter.ViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ParkingLotAdapter.ViewHolder {

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.card_item_parking_lot_info, parent, false))
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.



    }

    override fun getItemCount(): Int {
        return items.size
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.parkingName.text = items[position].name
       // holder.streetName = items[position].
       // holder.spotsAvailable.text = items[position]
      //To change body of created functions use File | Settings | File Templates.

    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each parking lot
        val parkingLotImage = view.card_parking_item_image
        val parkingName = view.parking_name
        val streetName = view.street_name
        val spotsAvailable = view.spots_available
    }



}