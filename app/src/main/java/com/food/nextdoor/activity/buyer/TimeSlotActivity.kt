package com.food.nextdoor.activity.buyer

import android.content.Intent
import com.food.nextdoor.R



import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.food.nextdoor.adapter.buyer.TimeSlotAdapter

import com.food.nextdoor.listeners.OnTimeSlotSelectListener
import com.food.nextdoor.model.DishItem
import com.food.nextdoor.model.TimeSlots
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_time_slot.*
import system.Manager
import system.ShoppingCart
import system.Utility

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TimeSlotActivity : AppCompatActivity(),OnTimeSlotSelectListener {

    private lateinit var m_dishItem: DishItem
    private val m_delimiter = "("



    private lateinit var timeSlotText: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_slot)

        val dishId = intent.getIntExtra(Utility.DISH_ID_KEY,0)
        m_dishItem =  DishItem()
        m_dishItem.dishId = dishId
        m_dishItem.quantity = 1

        val listOfCartItem = ShoppingCart.getCartItems()



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

        btn_confirm_slots.setOnClickListener { start() }
    }




    private fun start() {
//        val intent = Intent(this, HomeActivity::class.java)
//        startActivity(intent)

        m_dishItem.deliveryStartTime = "6.30pm"
        m_dishItem.deliveryEndTime = "6.45pm"

        val intent = Intent(this, HomeActivity:: class.java)
        intent.putExtra(Utility.DISH_ITEM_KEY, m_dishItem)
        this.startActivity(intent)
    }


    open fun onPackinOptionClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            var strPackingDescription = ""
            // Check which radio button was clicked
            when (view.getId()) {

                R.id.rb_packing_disposable ->
                    if (checked) {
                        strPackingDescription = rb_packing_disposable.text.toString()
                    }
                R.id.rb_packing_bring_own ->
                    if (checked) {
                        strPackingDescription = rb_packing_bring_own.text.toString()
                    }
            }
            Toast.makeText(applicationContext,"${strPackingDescription}", Toast.LENGTH_SHORT).show()
            m_dishItem.packingTypeId = Manager.Companion.Preference().getPackingTypeId(strPackingDescription.split(m_delimiter)[0].trim())
        }
    }


   open fun onDeliveryOptionClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked


            var strDeliveryDescription = ""
            // Check which radio button was clicked
            when (view.getId()) {
                R.id.rb_home_delivery ->
                    if (checked) {
                        strDeliveryDescription = rb_home_delivery.text.toString()
                    }
                R.id.rb_self_pick ->
                    if (checked) {
                        strDeliveryDescription = rb_self_pick.text.toString()
                    }
            }

            Toast.makeText(applicationContext,"${strDeliveryDescription}", Toast.LENGTH_SHORT).show()
            m_dishItem.deliveryTypeId = Manager.Companion.Preference().getDeliveryTypeId(strDeliveryDescription.split(m_delimiter)[0].trim())
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

//@Parcelize
//class PackingAndDeliveryWarper (val deliveryStartTime: String, val deliveryEndTime: String, val packingTypeId: Int, val deliveryTypeId: Int) :
//    Parcelable {
//
//}

