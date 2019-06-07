package com.food.nextdoor.activity.buyer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.food.nextdoor.R
import com.food.nextdoor.adapter.buyer.DishDetailAdapter
import com.food.nextdoor.model.Chef
import com.food.nextdoor.model.Dish

import com.food.nextdoor.model.HomeFeed
//import com.food.nextdoor.model.Chef
//import com.food.nextdoor.model.Dish
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dish_detail.*
import kotlinx.android.synthetic.main.home_row.*
import system.Utility


class DishDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dish_detail)

        // Extract dishId from intent and homeFeed from dataholder
        val dishId = intent.getIntExtra(Utility.DISH_ID_KEY,0)
        val buyerHomeFeed = Utility.DataHolder.homeFeedInstance
        supportActionBar?.title = dishId.toString()

        // Set Controls
         setControls(dishId, buyerHomeFeed)

        // Set Ongoing Recycle View with 4  rows Only
        setOngoingRecycleView(buyerHomeFeed)

    }


    fun loadAllDishes(view: View){
        startActivity(Intent(this, HomeActivity::class.java))
    }



    private fun setControls(dishId: Int, homeFeed: HomeFeed?){
        val dishInfo: Dish = homeFeed!!.dishes.filter { d-> d.dish_id == dishId}.single()
        val chefinfo: Chef = homeFeed.chefs.filter { c-> dishInfo.chef_id == c.chef_id}.single()

        // set main Dish Image
       Picasso.with(this).load(dishInfo.dish_image_url).into(img_dish_Image_main)

        // set Dish Symbol
        val dishSymbol =   Utility.setDishSymbol(dishInfo.dish_type,img_dish_symbol_detail_main.context)
        img_dish_symbol_detail_main.setImageDrawable(dishSymbol)



        tv_dish_name_detail_main.setText(dishInfo.dish_name)
        //tv_dish_price_home.setText(dishInfo.unit_price.toString())
        tv_dish_rating_detail_main.setText(dishInfo.dish_rating.toString())
        tv_dish_rating_bar_detail.rating = dishInfo.dish_rating.toFloat()
        tv_dish_description_detail_main.setText(dishInfo.dish_description)

        //tv_dish_reviews_home.setText("9999")
        //tv_dish_sold_home.setText(dishInfo.dishes_sold.toString())
        //tv_dish_available_home.setText(dishInfo.available_dishes.toString())
    }

    private fun setOngoingRecycleView(buyerHomeFeed: HomeFeed?) {
        recyclerView_dish_detail.layoutManager = LinearLayoutManager(this)
        recyclerView_dish_detail.isNestedScrollingEnabled = false
        var fourDishes: Array<Dish> = buyerHomeFeed!!.dishes.take(4).toTypedArray()
        buyerHomeFeed.dishes = fourDishes.toList()
        recyclerView_dish_detail.adapter = DishDetailAdapter(buyerHomeFeed)
    }





}
