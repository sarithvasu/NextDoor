package com.food.nextdoor.activity.buyer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.OrientationHelper
import com.food.nextdoor.R
import com.food.nextdoor.adapter.buyer.TimeSlotAdapter
import com.food.nextdoor.model.TimeSlots
import kotlinx.android.synthetic.main.activity_time_slot.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TimeSlotActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_slot)

        var timeSlots: TimeSlots = createTimeSlots("09:00 AM","07:00 PM",30)
        rv_after_noon_time_slots.apply {
            layoutManager = GridLayoutManager(this@TimeSlotActivity,2)
            adapter = TimeSlotAdapter(this@TimeSlotActivity,timeSlots.afterNoonTimeSlots)
        }
        rv_evening_time_slots.apply {
            layoutManager = GridLayoutManager(this@TimeSlotActivity,2)
            adapter = TimeSlotAdapter(this@TimeSlotActivity,timeSlots.evevingTimeSlots)
        }
        rv_morning_time_slots.apply {
            layoutManager = GridLayoutManager(this@TimeSlotActivity,2)
            adapter = TimeSlotAdapter(this@TimeSlotActivity,timeSlots.morningTimeSlots)
        }



    }

    private fun createTimeSlots(startTime: String, EndTime: String, intervel: Int): TimeSlots {

        val sdf = SimpleDateFormat("hh:mm a")
        var EndTime: Date = sdf.parse(EndTime)
        var currentDate: Date = sdf.parse(startTime)
        var noon: Date =sdf.parse("12:00 PM")
        var evening: Date =sdf.parse("04:01 PM")

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
}
