package com.food.nextdoor.activity.buyer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import com.food.nextdoor.R
import com.food.nextdoor.adapter.buyer.CheckoutAdapter
import com.food.nextdoor.adapter.buyer.DishDetailAdapter
import com.food.nextdoor.adapter.buyer.DishDetailViewHolder
import com.food.nextdoor.model.Dish
import kotlinx.android.synthetic.main.checkout.*
import kotlinx.android.synthetic.main.dish_detail.*

class CheckoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checkout)


//


        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView_checkout.layoutManager =  layoutManager
       //val checkoutAdpoter = CheckoutAdapter(this, myModelObjetc)
        //homeFeed.dishes= threeDish.toList()
        //recyclerView_checkout.adapter = checkoutAdpoter



    }



}
