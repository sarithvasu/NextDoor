package com.food.nextdoor.activity.seller

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.food.nextdoor.R
import com.food.nextdoor.database.NextDoorDB
import com.food.nextdoor.model.post.PostActiveDishForFirst
import com.food.nextdoor.model.post.ResponseCode
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.registration.*
import kotlinx.android.synthetic.main.seller_upload_secondary.*
import retrofit2.Call
import retrofit2.Response
import system.*
import system.Utility.Companion.NETWORK_STANDARD_TIME_2
import java.util.*

class SellerUploadSecondary : AppCompatActivity() {

    private lateinit var dateFromTo: DateFromTo
    private var fromDate = ""
    private var toDate = ""
    private var dishId = 0
    private var deliverCharge = 0
    private var packingCharges = 0
    private var timeSlot = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seller_upload_secondary)
        dishId = intent.getIntExtra(Utility.DISH_ID_KEY, 0)
        checkTimeValiddation()
        setFromTime()
        btn_continue_seller_upload_secondary.setOnClickListener {

           if(validationDaone()) {
               val str =
                   (findViewById(linearLayout12.checkedRadioButtonId) as RadioButton).text.toString().split(
                       "\\s".toRegex()
                   )[0].toInt()
               timeSlot = if (str == 1) 60 else str
               val cDStr = et_delivery_charge.text.toString()
               deliverCharge = if (cDStr.equals("")) 0 else cDStr.toInt()
               val cPStr = et_pacel_box.text.toString()
               deliverCharge = if (cPStr.equals("")) 0 else cPStr.toInt()
               activateDish()
           }
        }
    }

    private fun setFromTime() {
        val currentDateTime = Calendar.getInstance()
        val a = currentDateTime.get(Calendar.AM_PM);

        et_from_time_hours.setText(currentDateTime.get(Calendar.HOUR).toString())
        et_time_from_minutes.setText(currentDateTime.get(Calendar.MINUTE).toString())
        if(a == Calendar.AM){
            rb_from_time_am.isChecked=true
        }
        else{
            rb_from_time_pm.isChecked=true
        }
    }

    private fun checkTimeValiddation() {
        et_time_to_hours.addTextChangedListener(chekeHour)
        et_from_time_hours.addTextChangedListener(chekeHour)
        et_time_from_minutes.addTextChangedListener(chekMinutes)
        et_time_to_minutes.addTextChangedListener(chekMinutes)

    }
    val chekMinutes =object  : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            if( !p0.toString().equals("")) {
                val min = p0.toString().toInt()
                if (min > 59) {
                    Utility.createCustomDialog(
                        this@SellerUploadSecondary,
                        "Minutes can not be greaterthan 59",true
                    )
                }
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

    }
    val chekeHour =  object :TextWatcher{
        override fun afterTextChanged(p0: Editable?) {
            if( !p0.toString().equals("")) {
                val hour = p0.toString().toInt()
                if (hour > 12) {
                    Utility.createCustomDialog(
                        this@SellerUploadSecondary,
                        "Hour can not be greaterthan 12",true
                    )
                }
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

    }

    private fun activateDish() {
        val gson = Gson()
        val activeDish = createActiviDishForFirstTime()
        val s = gson.toJson(activeDish)
        busy_bar_seller_upload_second.visibility=VISIBLE
        val activateDishService = RetrofitInstantBuilder.buildService(RetrofitService::class.java)
        activateDishService?.postManegeActivateDish(
            "application/json",
            "application/json", activeDish
        ).also {
            it?.enqueue(object : retrofit2.Callback<ResponseCode> {
                override fun onFailure(call: Call<ResponseCode>, t: Throwable) {
                    val s = "sarith"
                    busy_bar_seller_upload_second.visibility= GONE
                }

                override fun onResponse(
                    call: Call<ResponseCode>,
                    response: Response<ResponseCode>
                ) {
                    busy_bar_seller_upload_second.visibility= GONE
                    if (response.isSuccessful && response.body() != null) {
                   val snackbar=     Snackbar.make(btn_continue_seller_upload_secondary,
                            "Dish activated sucesssful",Snackbar.LENGTH_INDEFINITE).setAction("OK",View.OnClickListener { finish() })
                        snackbar.view.setBackgroundColor(ContextCompat.getColor(this@SellerUploadSecondary, R.color.DarkBlue))
                        snackbar.show()




                    }
                }

            })
        }
    }

    private fun createActiviDishForFirstTime(): PostActiveDishForFirst {
        return PostActiveDishForFirst(
            DeliveryCharge = deliverCharge,
            // DeliveryOptions = NextDoorDB.invoke(this).daoAccess.getDeliveryTypeIdByDescription((findViewById(rg_self_pick_seller_upload_secondary.checkedRadioButtonId) as RadioButton).text.toString()),
            DeliveryOptions = getDelivaeryOptionId(),
            DishAvailableStartTime = getFromTime(),
            DishAvailableEndTime = getToTime(),
            UserId =  Preference(this).getUserId(),
            DishId = dishId,
            ChefId = Preference(this).getUserChefId(),
            PackingCharge = packingCharges,
            // PackingTypeId = NextDoorDB.invoke(this).daoAccess.getPackingTypeIdByDescription((findViewById(rg_packing_option_secondary.checkedRadioButtonId) as RadioButton).text.toString()),
            PackingOptions = getPackingOptionId(),
            QuantityPreparing = et_dish_quatity.text.toString().toInt(),
            TimeSlotInterval = timeSlot
        )
    }

    private fun getToTime(): String {
        if( !et_time_to_hours.text.toString().equals("")&&!et_time_to_minutes.text.toString().equals("")) {
            val currentDateTime = Calendar.getInstance()
            val startYear = currentDateTime.get(Calendar.YEAR)
            val startMonth = currentDateTime.get(Calendar.MONTH)
            val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
            if (rb_to_time_am.isChecked)
                currentDateTime.set(
                    startYear, startMonth, startDay,
                    et_time_to_hours.text.toString().toInt(),
                    et_time_to_minutes.text.toString().toInt()
                )
            else {
                if (et_time_to_hours.text.toString().toInt() != 12)
                    currentDateTime.set(
                        startYear, startMonth, startDay,
                        et_time_to_hours.text.toString().toInt() + 12,
                        et_time_to_minutes.text.toString().toInt()
                    )
                else
                    currentDateTime.set(
                        startYear, startMonth, startDay,
                        et_time_to_hours.text.toString().toInt(),
                        et_time_to_minutes.text.toString().toInt()
                    )

            }
            return Utility.fromCaldarToNetworkDate(currentDateTime)
        }
        return ""
    }

    private fun getFromTime(): String {
        if( !et_from_time_hours.text.toString().equals("")&&!et_time_from_minutes.text.toString().equals("")) {
            val currentDateTime = Calendar.getInstance()
            val startYear = currentDateTime.get(Calendar.YEAR)
            val startMonth = currentDateTime.get(Calendar.MONTH)
            val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)

            if (rb_from_time_am.isChecked)
                currentDateTime.set(
                    startYear, startMonth, startDay,
                    et_from_time_hours.text.toString().toInt(),
                    et_time_from_minutes.text.toString().toInt()
                )
            else {
                if (et_from_time_hours.text.toString().toInt() != 12) {
                    currentDateTime.set(
                        startYear, startMonth, startDay,
                        et_from_time_hours.text.toString().toInt() + 12,
                        et_time_from_minutes.text.toString().toInt()
                    )
                } else {
                    currentDateTime.set(
                        startYear, startMonth, startDay,
                        et_from_time_hours.text.toString().toInt(),
                        et_time_from_minutes.text.toString().toInt()
                    )
                }
            }
            return Utility.fromCaldarToNetworkDate(currentDateTime)
        }
        return ""
    }

    private fun getPackingOptionId(): Int {

        if (cb_own_box_seller_upload_secondary.isChecked && cb_parcel_seller_upload_secondary.isChecked) {
            return PakingOption.GET_YOUROWN_BOX.value or PakingOption.PARCEL_IN_DISPOSABLE_BOX.value
        } else if (cb_own_box_seller_upload_secondary.isChecked) {
            return PakingOption.GET_YOUROWN_BOX.value
        } else if (cb_parcel_seller_upload_secondary.isChecked) {
            return PakingOption.GET_YOUROWN_BOX.value
        } else {
            return 0
        }
    }

    private fun getDelivaeryOptionId(): Int {

        if (cb_home_delivery_seller_upload_secondary.isChecked && cb_self_pick_seller_upload_secondary.isChecked) {
            return DeliveryOption.HOME_DELIVERY.value or DeliveryOption.SELF_PICK.value
        } else if (cb_home_delivery_seller_upload_secondary.isChecked) {
            return DeliveryOption.HOME_DELIVERY.value
        } else if (cb_self_pick_seller_upload_secondary.isChecked) {
            return DeliveryOption.SELF_PICK.value
        } else {
            return 0
        }
    }

    private fun pickDateTime() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, day ->
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)
                setDateinUI(pickedDateTime)
            }, startHour, startMinute, false).show()
        }, startYear, startMonth, startDay).show()
    }

    private fun setDateinUI(pickedDateTime: Calendar?) {
        val date = Utility.fromCaldarToDate(pickedDateTime)
        if (dateFromTo == DateFromTo.TO) {
            textView84.text = "TO :$date"
            toDate = Utility.toNetworkDate(Utility.SELLER_DATE_TIME_FORMATE, date)
        } else if (dateFromTo == DateFromTo.FROM) {
            textView83.text = "From :$date"
            fromDate = Utility.toNetworkDate(Utility.SELLER_DATE_TIME_FORMATE, date)
        }

    }
    private fun validationDaone():Boolean{
        var msg=""
        val fromDate = Utility.getDateFromSting(getFromTime())
        val toDate = Utility.getDateFromSting(getToTime())
        val now = Utility.getDateFromSting(Utility.fromCaldarToNetworkDate(
            Calendar.getInstance()))
        if(fromDate==null||toDate==null){
            msg = "please give start and end time"
        }
        else if(fromDate!!.before((now))){
            msg = "from time already past."
        }
        else if(rg_from_am_pm.checkedRadioButtonId == -1||rg_to_am_pm.checkedRadioButtonId == -1){
            msg = "please give AM PM"
        }
        else if(fromDate!=null&&toDate!=null&&toDate!!.before(fromDate)){
            msg = "To time cannot be before From time"
        }
        else if(linearLayout12.checkedRadioButtonId == -1){
            msg = "Select a time slot"
        }
        else if(!cb_own_box_seller_upload_secondary.isChecked&&!cb_parcel_seller_upload_secondary.isChecked){
            msg = "Select any parcel option"
        }
        else if(!cb_home_delivery_seller_upload_secondary.isChecked&&!cb_self_pick_seller_upload_secondary.isChecked){
            msg = "Select any delivery option"
        }
        else if(cb_home_delivery_seller_upload_secondary.isChecked&&et_delivery_charge.text.toString().equals("")){
            msg = "delivery charge not given"
        }
        else if(cb_parcel_seller_upload_secondary.isChecked&&et_pacel_box.text.toString().equals("")){
            msg = "parcel charge not given"
        }
        else if(et_dish_quatity.text.toString().equals("")){
            msg = "Quatity prepaired is not given"
        }
        if(!msg.equals("")){
            Utility.createCustomDialog(this, msg ,true)
            return false
        }
        return true
    }
}


