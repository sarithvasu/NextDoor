package com.food.nextdoor.adapter.buyer

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.food.nextdoor.R

import kotlinx.android.synthetic.main.time_slot_row.view.*

class TimeSlotAdapter(val context: Context, val timeSlots:ArrayList<String>):RecyclerView.Adapter<TimeSlotViewHolder>() {
    var row_index:Int=-1
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TimeSlotViewHolder {
        return TimeSlotViewHolder(LayoutInflater.from(context).inflate(R.layout.time_slot_row,p0,false))
    }

    override fun getItemCount(): Int {
        return timeSlots.size
    }

    override fun onBindViewHolder(holder: TimeSlotViewHolder, position: Int) {
        holder.tv_time_slot.text=timeSlots.get(position)


        holder.tv_time_slot.setOnClickListener {
            row_index =position

            notifyDataSetChanged()
        }
        if(row_index==position){
            holder.tv_time_slot.setBackgroundColor(Color.BLUE)
            holder.tv_time_slot.setTextColor(Color.parseColor("#ffffff"))
        }
        else{
            holder.tv_time_slot.setBackgroundColor(Color.parseColor("#ffffff"))
            holder.tv_time_slot.setTextColor(Color.parseColor("#000000"))
        }



    }
}
class TimeSlotViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val tv_time_slot=view.tv_time_slot

}