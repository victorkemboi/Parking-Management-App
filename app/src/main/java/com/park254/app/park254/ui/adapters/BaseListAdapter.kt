package com.park254.app.park254.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.widget.BaseAdapter
import java.util.ArrayList

abstract class BaseListAdapter<M>(protected val context: Context) : BaseAdapter() {
    protected val inflater: LayoutInflater = LayoutInflater.from(context)
    protected val items: MutableList<M> = ArrayList()

    override fun getCount(): Int {
        return this.items.size
    }

    fun setItems(items: List<M>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun addItem(item: M) {
        this.items.add(item)
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): M {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun indexOf(item: M): Int {
        return items.indexOf(item)
    }

    fun clearItems() {
        this.items.clear()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        this.items.removeAt(position)
        notifyDataSetChanged()
    }

    fun removeItem(item: M) {
        this.items.remove(item)
        notifyDataSetChanged()
    }

}