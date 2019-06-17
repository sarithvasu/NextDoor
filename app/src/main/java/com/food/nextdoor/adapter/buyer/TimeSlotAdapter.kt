package com.food.nextdoor.adapter.buyer

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.food.nextdoor.R
import com.food.nextdoor.activity.buyer.TimeSlotActivity
import com.food.nextdoor.listeners.OnTimeSlotSelectListener

import kotlinx.android.synthetic.main.time_slot_row.view.*

class TimeSlotAdapter(val context: Context, val timeSlots:ArrayList<String>,val slotCategory:Int):RecyclerView.Adapter<TimeSlotViewHolder>() {
    companion object{
        val MORNING_SLOTS:Int=1;
        val AFTER_NOON_SLOTS:Int=2;
        val EVENING_SLOTS:Int=3;

    }
    open var row_index:Int=-1
    private lateinit var listener: OnTimeSlotSelectListener
    init{
        this.listener= this.context as TimeSlotActivity
    }
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
            listener.updateOther(slotCategory,holder.tv_time_slot.text.toString())
            notifyDataSetChanged()
        }
        if(row_index==position){
            //holder.tv_time_slot.setBackgroundColor(Color.BLUE)
            holder.tv_time_slot.setTextColor(Color.GREEN)
        }
        else{
            //holder.tv_time_slot.setBackgroundColor(Color.parseColor("#ffffff"))
            holder.tv_time_slot.setTextColor(Color.parseColor("#000000"))
        }



    }


}
class TimeSlotViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val tv_time_slot=view.tv_time_slot

}
