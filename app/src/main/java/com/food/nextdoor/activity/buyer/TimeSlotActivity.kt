package com.food.nextdoor.activity.buyer

import android.content.Intent
import com.food.nextdoor.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.ViewCompat
import com.food.nextdoor.adapter.buyer.DishDetailAdapter
import com.food.nextdoor.adapter.buyer.TimeSlotAdapter
import com.food.nextdoor.database.NextDoorDB
import com.food.nextdoor.listeners.OnTimeSlotSelectListener

import com.food.nextdoor.model.HomeFeed
import com.food.nextdoor.model.TimeSlots
import com.food.nextdoor.model.post.DishItem
import kotlinx.android.synthetic.main.activity_time_slot.*
import system.*
import java.util.*
import kotlin.collections.ArrayList
import java.text.SimpleDateFormat as SimpleDateFormat1


class TimeSlotActivity : AppCompatActivity(),OnTimeSlotSelectListener {

    private lateinit var mDishItem: DishItem
    private val mDelimiter = "("
    private val mDelimiterHyphen  = "-"

    private  var timeSlotText: String =""

  //  private var isNotHomeDelivery=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_slot)

        val dishId = intent.getIntExtra(Utility.DISH_ID_KEY,0)
        val chefId = intent.getIntExtra(Utility.CHEF_ID_KEY,0)
        //val dishInfo: HomeFeed.Dish =  Utility.DataHolder.homeFeedInstance!!.signatureDishes.filter { d-> d.DishId == dishId && d.DishId == 5 }.single()

        val dishInfo: HomeFeed.Dish? =  DataHolder.homeFeedInstance.dishes.find { d-> d.DishId == dishId && d.ChefId == chefId  }
        if (dishInfo == null) {
            // Soumen need to throw error msg
        }

        if(dishInfo?.DeliveryOptions == DeliveryOption.BOTH.value){
            rb_home_delivery.text = "${DeliveryDescription.DESC_HOME_DELIVERY.value} ( Rs ${dishInfo?.DeliveryCharge} Extra)"
            rb_self_pick.text=DeliveryDescription.DESC_SELF_PICK.value
            rb_home_delivery.isEnabled=true
            rb_self_pick.isEnabled=true
        }
        else if(dishInfo?.DeliveryOptions==DeliveryOption.HOME_DELIVERY.value){
            rb_home_delivery.text = "${DeliveryDescription.DESC_HOME_DELIVERY.value} ( Rs ${dishInfo?.DeliveryCharge} Extra)"
            rb_home_delivery.isEnabled=true
        }
        else if(dishInfo?.DeliveryOptions==DeliveryOption.SELF_PICK.value){
            rb_home_delivery.isEnabled=false
        }

        if(dishInfo?.PackingOptions==PakingOption.BOTH.value) {
            rb_packing_disposable.text = "${PakingDescription.DESC_PARCEL_IN_DISPOSABLE_BOX} ( Rs ${dishInfo?.PackageCharge} Extra)"
            rb_packing_bring_own.text = PakingDescription.DESC_GET_YOUROWN_BOX.value
            rb_packing_disposable.isEnabled=true
            rb_packing_bring_own.isEnabled=true
        }
        else if (dishInfo?.PackingOptions==PakingOption.PARCEL_IN_DISPOSABLE_BOX.value){
            rb_packing_disposable.text = "${PakingDescription.DESC_PARCEL_IN_DISPOSABLE_BOX} ( Rs ${dishInfo?.PackageCharge} Extra)"
            rb_packing_disposable.isEnabled=true
        }
        else if (dishInfo?.PackingOptions==PakingOption.GET_YOUROWN_BOX.value){
            rb_packing_disposable.isEnabled=false
        }



      /*  if(dishInfo?.PackingDescription!=null&&!dishInfo.PackingDescription.equals("")) {
            rb_packing_disposable.text = "${dishInfo?.PackingDescription} ( Rs ${dishInfo?.PackageCharge} Extra)"
        }
        else
            rb_packing_disposable.isEnabled=false

        if(dishInfo?.DeliveryDescription!=null&&!dishInfo.DeliveryDescription.equals("")) {
            rb_home_delivery.text = "${dishInfo?.DeliveryDescription} ( Rs ${dishInfo?.DeliveryCharge} Extra)"

        }
        else {
            rb_home_delivery.isEnabled = false
         //   isNotHomeDelivery=true
        }*/



        mDishItem =  DishItem()
        mDishItem.dishId = dishId
        mDishItem.quantity = 1
        mDishItem.unitPrice = dishInfo!!.UnitPrice
        mDishItem.chefId = dishInfo.ChefId
        mDishItem.deliveryEndTime=dishInfo.DishAvailableEndTime
        mDishItem.deliveryStartTime=dishInfo.DishAvailableStartTime


        val timeSlots: TimeSlots= createTimeSlots(dishInfo.DishAvailableStartTime,dishInfo.DishAvailableEndTime,dishInfo.TimeSlotInterval)

        rv_after_noon_time_slots.apply {
            layoutManager = androidx.recyclerview.widget.GridLayoutManager(this@TimeSlotActivity, 2)
            adapter = TimeSlotAdapter(this@TimeSlotActivity,timeSlots.afterNoonTimeSlots,TimeSlotAdapter.AFTER_NOON_SLOTS)
        }

        ViewCompat.setNestedScrollingEnabled(nsc_morninf_time_slot,true)
        rv_evening_time_slots.apply {
            layoutManager = androidx.recyclerview.widget.GridLayoutManager(this@TimeSlotActivity, 2)
            adapter = TimeSlotAdapter(this@TimeSlotActivity,timeSlots.eveningTimeSlots,TimeSlotAdapter.EVENING_SLOTS)
        }
        rv_morning_time_slots.apply {
            layoutManager = androidx.recyclerview.widget.GridLayoutManager(this@TimeSlotActivity, 2)
            adapter = TimeSlotAdapter(this@TimeSlotActivity,timeSlots.morningTimeSlots,TimeSlotAdapter.MORNING_SLOTS)
        }
        ViewCompat.setNestedScrollingEnabled(nsc_morninf_time_slot,false)
        ViewCompat.setNestedScrollingEnabled(nsc_after_noon_time_slot,false)
        ViewCompat.setNestedScrollingEnabled(nsc_evening_time_slot,false)
        ViewCompat.setNestedScrollingEnabled(rv_after_noon_time_slots,false)
        ViewCompat.setNestedScrollingEnabled(rv_evening_time_slots,false)
        ViewCompat.setNestedScrollingEnabled(rv_morning_time_slots,false)
        btn_confirm_slots.setOnClickListener { start() }
    }




    private fun start() {
        if(!timeSlotText.equals("")&& mDishItem.packingTypeId!=-1&&mDishItem.deliveryTypeId !=-1) {

            mDishItem.deliveryStartTime = getConvertedDate( mDishItem.deliveryStartTime, timeSlotText.split(mDelimiterHyphen)[0].trim())
            mDishItem.deliveryEndTime =  getConvertedDate(mDishItem.deliveryStartTime,timeSlotText.split(mDelimiterHyphen)[1].trim())

           // or we can use directly  val intent = Intent(this, callingActivity.className::class.java)
        /*    if (this.callingActivity.className.toString() == "com.food.nextdoor.activity.buyer.DishDetailActivity") {
                val intent = Intent(this, DishDetailActivity::class.java)
                intent.putExtra(Utility.DISH_ITEM_KEY, mDishItem)
                setResult(DishDetailAdapter.REQUEST_CODE, intent)
                finish()
            } else if (this.callingActivity.className.toString() == "com.food.nextdoor.activity.buyer.HomeActivity") {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra(Utility.DISH_ITEM_KEY, mDishItem)
                setResult(HomeAdapter.REQUEST_CODE, intent)
                finish()
            }*/

            //no need to specify a actity when setting a result
            val intent = Intent()
            intent.putExtra(Utility.DISH_ITEM_KEY, mDishItem)
            setResult(DishDetailAdapter.REQUEST_CODE, intent)
            finish()
        }

        else{
            if(timeSlotText.equals(""))
            this.validateTimeSlotsScreen()
            else if (mDishItem.packingTypeId==-1)
                Toast.makeText(this,"Please select a packing option",Toast.LENGTH_SHORT).show()
            else if (mDishItem.deliveryTypeId ==-1)
                Toast.makeText(this,"Please select a delivery option",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getConvertedDate(deliveryStartTime: String?, trim: String): String? {
        deliveryStartTime?.split("T")?.get(0)
        return "${deliveryStartTime?.split("T")?.get(0)}T${Utility.change24hoursTimeSlot(trim)}"
    }

    private fun validateTimeSlotsScreen() {
        if (timeSlotText.isEmpty()) {
            Toast.makeText(this,"Please select a delivery time",Toast.LENGTH_SHORT).show()
        }

//        if (mDishItem.deliveryTypeId == null) {
//            Toast.makeText(this,"Please select a delivery option",Toast.LENGTH_SHORT).show()
//        }
//
//        if (mDishItem.packingTypeId == null) {
//            Toast.makeText(this,"Please select a packing option",Toast.LENGTH_SHORT).show()
//        }
    }


     fun onPackinOptionClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            var strPackingDescription = ""
            // Check which radio button was clicked
            when (view.getId()) {

                R.id.rb_packing_disposable ->
                    if (checked) {

                        strPackingDescription = rb_packing_disposable.text.toString()
                       // if(!isNotHomeDelivery)
                       // rb_home_delivery.isEnabled=true

                    }
                R.id.rb_packing_bring_own ->
                    if (checked) {
                        strPackingDescription = rb_packing_bring_own.text.toString()
                      //  if(!isNotHomeDelivery)
                      //  rb_home_delivery.isEnabled=false
                     //   rb_self_pick.isChecked=true

                    }
            }
            //Toast.makeText(applicationContext,"${strPackingDescription}", Toast.LENGTH_SHORT).show()
            mDishItem.packingTypeId = NextDoorDB.invoke(this).daoAccess.getPackingTypeIdByDescription(strPackingDescription.split(mDelimiter)[0].trim())
        }
    }


   fun onDeliveryOptionClicked(view: View) {
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

           // Toast.makeText(applicationContext,"${strDeliveryDescription}", Toast.LENGTH_SHORT).show()
            val strDescription = strDeliveryDescription.split(mDelimiter)[0].trim()
            mDishItem.deliveryTypeId = NextDoorDB.invoke(this).daoAccess.getDeliveryTypeIdByDescription(strDescription)
        }
    }


    private fun createTimeSlots(startTimeNetwork: String, EndTimeNetwok: String, intervel: Int): TimeSlots {

        val sdf = SimpleDateFormat1(Utility.HH_MM_A)
        val EndTime=Utility.standardDateToTimeSlot(EndTimeNetwok)
        val startTime=Utility.standardDateToTimeSlot(startTimeNetwork)
        val endTime:Date = sdf.parse(EndTime)
        var currentDate: Date = sdf.parse(startTime)
        val noon:Date=sdf.parse("12:00 PM")
        val evening:Date=sdf.parse("04:01 PM")

        val timeSlotsMorning :ArrayList<String> = arrayListOf()
        val timeSlotsafterNoon :ArrayList<String> = arrayListOf()
        val timeSlotsEvening :ArrayList<String> = arrayListOf()
        val timeSlots =TimeSlots(timeSlotsMorning,timeSlotsafterNoon,timeSlotsEvening)

        while(currentDate.before(endTime)) {
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



