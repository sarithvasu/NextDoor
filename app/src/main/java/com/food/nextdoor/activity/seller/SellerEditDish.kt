package com.food.nextdoor.activity.seller

import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import com.food.nextdoor.R
import com.food.nextdoor.database.NextDoorDB
import com.food.nextdoor.model.DishFeed
import com.food.nextdoor.model.post.PostEditInactiveDish
import com.food.nextdoor.model.post.ResponseCode
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.seller_edit_dish_details.*
import kotlinx.android.synthetic.main.seller_upload_secondary.*
import retrofit2.Call
import retrofit2.Response
import system.Utility

class SellerEditDish : AppCompatActivity() {

    private lateinit var dishFeed: DishFeed
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seller_edit_dish_details)
        dishFeed = intent.getParcelableExtra(Utility.DISH_FEED)
        setControl()
        tv_save_seller_edit_dish_details.setOnClickListener {
            postInActiveDish()
        }
    }

    private fun postInActiveDish() {
        val postInactiveDishService =
            RetrofitInstantBuilder.buildService(RetrofitService::class.java)
        postInactiveDishService?.postEditInactiveDish(
            "application/json",
            "application/json", getpostEditInActiveDish()
        ).also {
            it?.enqueue(object : retrofit2.Callback<ResponseCode> {
                override fun onFailure(call: Call<ResponseCode>, t: Throwable) {
                    Snackbar.make(tv_save_seller_edit_dish_details,"error ${t.localizedMessage}",Snackbar.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<ResponseCode>,
                    response: Response<ResponseCode>
                ) {
                   if(response.isSuccessful&&response.body()!=null){
                       Snackbar.make(tv_save_seller_edit_dish_details,"upadated",Snackbar.LENGTH_SHORT).show()
                       finish()
                   }
                }

            })
        }

    }

    private fun getpostEditInActiveDish(): PostEditInactiveDish {
        return PostEditInactiveDish(
            DeliveryCharge = tv_delivery_charges_seller_edit_dish_details.text.toString().toInt(),
            TimeSlotInterval = (findViewById(linearLayout25.checkedRadioButtonId) as RadioButton).text.toString().split(
                "\\s".toRegex()
            )[0].toInt(),
            DeliveryTypeId = 2,
            DishDescription = tv_dish_description_seller_edit_dish_details.text.toString(),
            DishId = dishFeed.DishId,
            PackingTypeId = 2,
            DishImageUrl = dishFeed.DishImageUrl,
            DishType = (findViewById(ll_dish_type.checkedRadioButtonId) as RadioButton).text.toString(),
            EarningAfterServiceCharge = dishFeed.EarningAfterServiceCharge,
            DishName = tv_dish_name_seller_edit_dish_details.text.toString(),
            MealType = (findViewById(radioGroup2.checkedRadioButtonId) as RadioButton).text.toString(),
            PlatformServiceChargePercentage = dishFeed.PlatformServiceChargePercentage,
            QuantityPreparing = tv_quantity_preparing_seller_edit_dish_details.text.toString().toInt(),
            UnitPrice = tv_dish_price_seller_edit_dish_details.text.toString().toInt(),
            ServingsPerPlate = tv_serving_per_plaate_seller_edit_dish_details.text.toString().toInt(),
            DishAvailableEndTime = Utility.fromSellerTimelotToToNetwork( textView200.text.toString().trim()),
            DishAvailableStartTime = Utility.fromSellerTimelotToToNetwork( textView157.text.toString().trim()),
            PackingCharge = tv_packing_chaerges_seller_edit_dish_details.text.toString().toInt()
        )
    }

    private fun setControl() {
        tv_dish_name_seller_edit_dish_details.setText(dishFeed.DishName)
        tv_dish_description_seller_edit_dish_details.setText(dishFeed.DishDescription)
        tv_dish_price_seller_edit_dish_details.setText(dishFeed.UnitPrice.toString())
        tv_quantity_preparing_seller_edit_dish_details.setText(dishFeed.QuantityPreparing.toString())
        tv_serving_per_plaate_seller_edit_dish_details.setText(dishFeed.ServingsPerPlate.toString())
        tv_delivery_charges_seller_edit_dish_details.setText(dishFeed.DeliveryCharge.toString())
        tv_packing_chaerges_seller_edit_dish_details.setText(dishFeed.PackingCharge.toString())
        textView157.text = Utility.fromNetworkToToSellerTimelot(dishFeed.DishAvailableStartTime)
        textView200.text = Utility.fromNetworkToToSellerTimelot(dishFeed.DishAvailableEndTime)
        tv_earnings_seller_edit_dish_details.text = dishFeed.EarningAfterServiceCharge.toString()
        setChekedForDishType(dishFeed.DishType)
        setChekedForMealType(dishFeed.MealType)
        setChekedForTimeSlot(dishFeed.TimeSlotInterval)
        setPackingCheckbox(dishFeed.PackingDescription)
        setDeliveryCheckbox(dishFeed.DeliveryDescription)
        Picasso.with(this).load(dishFeed.DishImageUrl).into(dish_image_seller_edit_dish_details)

    }

    private fun setDeliveryCheckbox(deliveryDescription: String) {
        when (deliveryDescription) {
            "Home delivery" -> tv_delivery_home_delivery_seller_edit_dish_details.isChecked = true
            "Self pick" -> tv_delivery_self_pick_seller_edit_dish_details.isChecked = true
        }
    }

    private fun setPackingCheckbox(packingDescription: String) {
        when (packingDescription) {
            "Get your own box" -> tv_packing_ownbox_seller_edit_dish_details.isChecked = true
            "Parcel in disposable box" -> tv_packing_parcel_seller_edit_dish_details.isChecked =
                true
        }
    }

    private fun setChekedForTimeSlot(timeSlotInterval: Int) {
        linearLayout25.forEach {
            if (it is RadioButton && (it as RadioButton).text.contains(timeSlotInterval.toString())) {
                it.isChecked = true
            }
        }
    }

    private fun setChekedForMealType(mealType: String) {
        radioGroup2.forEach {
            if (it is RadioButton && (it as RadioButton).text.equals(mealType)) {
                it.isChecked = true
            }
        }
    }

    private fun setChekedForDishType(dishType: String) {
        ll_dish_type.forEach {
            if (it is RadioButton && (it as RadioButton).text.equals(dishType)) {
                it.isChecked = true
            }
        }
    }
}
