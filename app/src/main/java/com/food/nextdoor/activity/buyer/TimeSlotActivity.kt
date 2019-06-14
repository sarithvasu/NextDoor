package com.food.nextdoor.activity.buyer

import com.food.nextdoor.R



import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.OrientationHelper
import android.text.format.DateUtils
import com.food.nextdoor.adapter.buyer.TimeSlotAdapter

import com.food.nextdoor.listeners.OnTimeSlotSelectListener
import com.food.nextdoor.model.TimeSlots
import kotlinx.android.synthetic.main.activity_time_slot.*

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TimeSlotActivity : AppCompatActivity(),OnTimeSlotSelectListener {

    private lateinit var timeSlotText: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_slot)

        var timeSlots: TimeSlots= createTimeSlots("09:00 AM","07:00 PM",60)
        rv_after_noon_time_slots.apply {
            layoutManager = GridLayoutManager(this@TimeSlotActivity,2)
            adapter = TimeSlotAdapter(this@TimeSlotActivity,timeSlots.afterNoonTimeSlots,TimeSlotAdapter.AFTER_NOON_SLOTS)
        }
        rv_evening_time_slots.apply {
            layoutManager = GridLayoutManager(this@TimeSlotActivity,2)
            adapter = TimeSlotAdapter(this@TimeSlotActivity,timeSlots.evevingTimeSlots,TimeSlotAdapter.EVENING_SLOTS)
        }
        rv_morning_time_slots.apply {
            layoutManager = GridLayoutManager(this@TimeSlotActivity,2)
            adapter = TimeSlotAdapter(this@TimeSlotActivity,timeSlots.morningTimeSlots,TimeSlotAdapter.MORNING_SLOTS)
        }




    }

    private fun createTimeSlots(startTime: String, EndTime: String, intervel: Int): TimeSlots {

        val sdf = SimpleDateFormat("hh:mm a")
        var EndTime:Date = sdf.parse(EndTime)
        var currentDate: Date = sdf.parse(startTime)
        var noon:Date=sdf.parse("12:00 PM")
        var evening:Date=sdf.parse("04:01 PM")

        var timeSlotsMorning :ArrayList<String> = arrayListOf()
        var timeSlotsafterNoon :ArrayList<String> = arrayListOf()
        var timeSlotsEvening :ArrayList<String> = arrayListOf()
        var timeSlots:TimeSlots=TimeSlots(timeSlotsMorning,timeSlotsafterNoon,timeSlotsEvening)

        while(currentDate.before(EndTime)) {
            val calendar = Calendar.getInstance()
            calendar.time = currentDate
            val strDate = sdf.format(currentDate)
            calendar.add(Calendar.MINUTE, intervel)
            currentDate = calendar.time
            val endDate = sdf.format(currentDate)
            if(currentDate.after(noon)&&currentDate.before(evening)) {
                timeSlotsafterNoon.add(strDate.toUpperCase() + "-" + endDate.toUpperCase())
            }
            else if(currentDate.after(evening)){
                timeSlotsEvening.add(strDate.toUpperCase() + "-" + endDate.toUpperCase())
            }
            else{
                timeSlotsMorning.add(strDate.toUpperCase() + "-" + endDate.toUpperCase())
            }
        }
        return timeSlots
    }
    override fun updateOther(recyclerViewOfType: Int, timeSlotText: String) {
        this.timeSlotText=timeSlotText
        if(recyclerViewOfType==TimeSlotAdapter.MORNING_SLOTS){
            val adapter :TimeSlotAdapter=rv_evening_time_slots.adapter as TimeSlotAdapter
            adapter.row_index=-1
            adapter.notifyDataSetChanged()
            val adapter1:TimeSlotAdapter=rv_after_noon_time_slots.adapter as TimeSlotAdapter
            adapter1.row_index=-1
            adapter1.notifyDataSetChanged()
        }
        else if(recyclerViewOfType==TimeSlotAdapter.EVENING_SLOTS){
            val adapter :TimeSlotAdapter=rv_morning_time_slots.adapter as TimeSlotAdapter
            adapter.row_index=-1
            adapter.notifyDataSetChanged()
            val adapter1:TimeSlotAdapter=rv_after_noon_time_slots.adapter as TimeSlotAdapter
            adapter1.row_index=-1
            adapter1.notifyDataSetChanged()
        }
        else if(recyclerViewOfType==TimeSlotAdapter.AFTER_NOON_SLOTS){
            val adapter :TimeSlotAdapter=rv_evening_time_slots.adapter as TimeSlotAdapter
            adapter.row_index=-1
            adapter.notifyDataSetChanged()
            val adapter1:TimeSlotAdapter=rv_morning_time_slots.adapter as TimeSlotAdapter
            adapter1.row_index=-1
            adapter1.notifyDataSetChanged()
        }
    }

}
