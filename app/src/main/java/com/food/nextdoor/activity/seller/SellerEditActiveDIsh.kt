package com.food.nextdoor.activity.seller

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.food.nextdoor.R
import com.food.nextdoor.model.DishFeed
import com.food.nextdoor.model.post.PostEditActiveDish
import com.food.nextdoor.model.post.ResponseCode
import com.food.nextdoor.webservices.RetrofitInstantBuilder
import com.food.nextdoor.webservices.RetrofitService
import kotlinx.android.synthetic.main.seller_edit_active_dish_details.*
import retrofit2.Call
import retrofit2.Response
import system.Utility

class SellerEditActiveDIsh : AppCompatActivity() {

    private lateinit var dishFeed: DishFeed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seller_edit_active_dish_details)
        dishFeed = intent.getParcelableExtra(Utility.DISH_FEED)
        setControl()
        tv_edit_active_dish.setOnClickListener {
            callEditActiveDish()
        }
    }

    private fun callEditActiveDish() {
        val editActiveDishService=RetrofitInstantBuilder.buildService(RetrofitService::class.java)
        editActiveDishService?.postEditActivatedDish("application/json",
            "application/json",createEditActiveDish()).also {
            it?.enqueue(object : retrofit2.Callback<ResponseCode>{
                override fun onFailure(call: Call<ResponseCode>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ResponseCode>,
                    response: Response<ResponseCode>
                ) {
                    if(response.isSuccessful&&response.body()!=null){
                        Utility.createCustomDialog(this@SellerEditActiveDIsh,"Edited successfully",View.OnClickListener {

                            finish()
                        })
                    }
                    else{
                        Utility.createCustomDialog(this@SellerEditActiveDIsh,"Something went wrong",View.OnClickListener {

                            finish()
                        })
                    }
                }

            })
        }
    }

    private fun createEditActiveDish(): PostEditActiveDish {
        return PostEditActiveDish(DishAvailableStartTime =Utility.fromSellerTimelotToToNetwork( tv_start_time.text.toString()),DishAvailableEndTime = Utility.fromSellerTimelotToToNetwork( tv_end_time.text.toString()),QuantityPreparing = editText11.text.toString().toInt(),DishId = dishFeed.DishId)
    }

    private fun setControl() {
        editText11.setText(dishFeed.QuantityPreparing.toString())
        tv_start_time.text=Utility.fromNetworkToToSellerTimelot(dishFeed.DishAvailableStartTime)
        tv_end_time.text=Utility.fromNetworkToToSellerTimelot(dishFeed.DishAvailableEndTime)

    }
}
