package com.park254.app.park254.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.park254.app.park254.R
import com.park254.app.park254.models.User
import com.park254.app.park254.utils.ItemAnimation
import kotlinx.android.synthetic.main.item_employee.view.*
import java.util.ArrayList

class EmployeeListAdapter(private val ctx: Context, items: ArrayList<User>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = ArrayList<User>()
    private var mOnItemClickListener: OnItemClickListener? = null

    private var lastPosition = -1
    private var on_attach = true

    var onItemClick: ((User) -> Unit)? = null

    interface OnItemClickListener {
        fun onItemClick(view: View, obj: User, position: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mItemClickListener
    }

    init {
        this.items = items
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

            val p = items[position]
            holder.name.text = p.userName
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