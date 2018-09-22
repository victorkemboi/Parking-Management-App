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
import com.park254.app.park254.models.Employee
import com.park254.app.park254.models.User
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.utils.ItemAnimation
import com.park254.app.park254.utils.livedata_adapter.ApiResponse
import kotlinx.android.synthetic.main.item_employee.view.*
import java.util.ArrayList
import javax.inject.Inject

class EmployeeListAdapter(private val ctx: Context, items: ArrayList<User>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = ArrayList<User>()
    private var mOnItemClickListener: OnItemClickListener? = null

    private var lastPosition = -1
    private var on_attach = true

    var onItemClick: ((User) -> Unit)? = null

    @Inject
    lateinit var retrofitApiService: RetrofitApiService

    interface OnItemClickListener {
        fun onItemClick(view: View, obj: User, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mItemClickListener
    }

    init {
        this.items = items

        (ctx.applicationContext as App).applicationInjector.inject(this)
    }

    inner class OriginalViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var employee_avatar = v.employee_avatar
        var name = v.employee_name
        var lyt_parent = v.lyt_parent_emp as View
        var email = v.emp_email
        var commence_date = v.commence_date

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(items[adapterPosition])
            }
        }


    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_employee, parent, false)
        vh = OriginalViewHolder(v)
        return vh
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.e("onBindViewHolder", "onBindViewHolder : $position")
        if (holder is OriginalViewHolder) {


          /*  retrofitApiService.getParkingLotEmployees(viewM).observe(
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
            ) */

            val p = items[position]
            holder.name.text = p.name
            holder.email.text = p.email

            Glide.with(ctx).load(p.photoUrl).into(holder.employee_avatar)



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