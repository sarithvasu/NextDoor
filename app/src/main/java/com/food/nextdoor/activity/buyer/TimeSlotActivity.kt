package com.food.nextdoor.activity.buyer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.OrientationHelper
import com.food.nextdoor.R
import com.food.nextdoor.adapter.buyer.TimeSlotAdapter
import kotlinx.android.synthetic.main.activity_time_slot.*

class TimeSlotActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_slot)

        val morningSlots=arrayListOf("09:00AM-09:15AM","09:15AM-09:30AM","09:30AM-09:45AM","09:45AM-10:00AM","10:00AM-10:15AM","10:15AM-10:30AM","10:30AM-10:45AM","10:45AM-11:00AM","11:00AM-11:15AM","11:15AM-11:30AM","11:30AM-11:45AM","11:45AM-12:00PM")
        val afterNoonSlots=arrayListOf("12:00PM-12:15PM","12:15PM-12:30PM","12:30PM-12:45PM","12:45PM-01:00PM","01:00PM-01:15PM","01:15PM-01:30PM","01:30PM-01:45PM","01:45PM-02:00PM","02:00PM-02:15PM","02:15PM-02:30PM","02:30PM-02:45PM","02:45PM-03:00PM")
        val eveningSlots=arrayListOf("04:00PM-04:15PM","04:15PM-04:30PM","04:30PM-04:45PM","04:45PM-05:00PM","05:00PM-05:15PM","05:15PM-05:30PM","05:30PM-05:45PM","05:45PM-06:00PM","06:00PM-06:15PM","06:15PM-06:30PM","06:30PM-06:45PM","06:45PM-07:00PM")


        rv_after_noon_time_slots.apply {
            layoutManager = GridLayoutManager(this@TimeSlotActivity,2, OrientationHelper.HORIZONTAL,false)
            adapter = TimeSlotAdapter(this@TimeSlotActivity,afterNoonSlots)
        }
        rv_evening_time_slots.apply {
            layoutManager = GridLayoutManager(this@TimeSlotActivity,2, OrientationHelper.HORIZONTAL,false)
            adapter = TimeSlotAdapter(this@TimeSlotActivity,eveningSlots)
        }
        rv_morning_time_slots.apply {
            layoutManager = GridLayoutManager(this@TimeSlotActivity,2, OrientationHelper.HORIZONTAL,false)
            adapter = TimeSlotAdapter(this@TimeSlotActivity,morningSlots)
        }



    }
}
