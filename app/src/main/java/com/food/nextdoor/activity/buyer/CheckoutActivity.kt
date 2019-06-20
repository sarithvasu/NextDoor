package com.food.nextdoor.activity.buyer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.food.nextdoor.R
import com.food.nextdoor.adapter.buyer.CheckoutAdapter
import com.food.nextdoor.model.HomeFeed
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.checkout.*
import kotlinx.android.synthetic.main.dish_detail.*
import system.Manager
import system.ShoppingCart
import system.Utility


class CheckoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checkout)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView_checkout.layoutManager =  layoutManager


        recyclerView_checkout.apply {
            recyclerView_checkout.adapter = CheckoutAdapter(this@CheckoutActivity)
        }


        // Set Controls
        setControls()









    }


    private fun setControls(){

//        val dishInfo: HomeFeed.Dish = homeFeed!!.dishes.filter { d-> d.dish_id == ShoppingCart}.single()
//        val chefinfo: HomeFeed.Chef = homeFeed.chefs.filter { c-> dishInfo.chef_id == c.chef_id}.single()
        val buyerInfo = Manager.Companion.buyerInfo

        // img_chef_profile_checkout and dishes_served need to add
//        tv_chef_name_checkout.setText(chefinfo.chef_name)
//        tv_chef_address_with_flat_number_checkout.setText(chefinfo.chef_flat_number)
//        tv_dishes_served_checkout.setText("Need to add")

        // img_buyer_profile_checkout neeed toa add
        tv_buyer_name_checkout.setText( buyerInfo.userName)
        tv_buyer_address_with_flat_number_checkout.setText( buyerInfo.flatNumber)
        tv_buyer_email_checkout.setText( buyerInfo.email)
        tv_buyer_phone_number_checkout.setText( buyerInfo.mobileNumber)

    }



}
