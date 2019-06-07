package com.food.nextdoor.adapter.buyer

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.food.nextdoor.R

import kotlinx.android.synthetic.main.time_slot_row.view.*

class TimeSlotAdapter(val context: Context, val timeSlots:ArrayList<String>):RecyclerView.Adapter<TimeSlotViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TimeSlotViewHolder {
        return TimeSlotViewHolder(LayoutInflater.from(context).inflate(R.layout.time_slot_row,p0,false))
    }

    override fun getItemCount(): Int {
      return timeSlots.size
    }

    override fun onBindViewHolder(holder: TimeSlotViewHolder, position: Int) {
       holder.tv_time_slot.text=timeSlots.get(position)
    }

}
class TimeSlotViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val tv_time_slot=view.tv_time_slot

}