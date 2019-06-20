package com.food.nextdoor.activity.buyer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TableLayout
import com.food.nextdoor.R
import com.food.nextdoor.adapter.buyer.DishDetailAdapter
import com.food.nextdoor.model.HomeFeed
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dish_detail.*
import kotlinx.android.synthetic.main.dish_detail.view.*
import kotlinx.android.synthetic.main.home.*
import system.CartItem
import system.ShoppingCart
import system.Utility


class DishDetailActivity : AppCompatActivity() {

    private  var listOfCartItem: ArrayList<CartItem> = ArrayList<CartItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.dish_detail)

        // Extract dishId from intent and homeFeed from dataholder
        val dishId = intent.getIntExtra(Utility.DISH_ID_KEY,0)
        val buyerHomeFeed = Utility.DataHolder.homeFeedInstance
        supportActionBar?.title = dishId.toString()

        listOfCartItem = ShoppingCart.getCartItems()
        if(listOfCartItem.size>0){
            floating_proced_lay_dish.visibility = View.VISIBLE
            var layout:RelativeLayout.LayoutParams=RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT)
            layout.bottomMargin=70
            nestedScrollView2.layoutParams=layout
        }else{
            floating_proced_lay_dish.visibility= View.GONE
            var layout:RelativeLayout.LayoutParams=RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT)
            layout.bottomMargin=0
            nestedScrollView2.layoutParams=layout
        }

        // Set Controls
         setControls(dishId, buyerHomeFeed)

        // Set Ongoing Recycle View with 4  rows Only
        setOngoingRecycleView(buyerHomeFeed)

    }

    override fun onResume() {
        super.onResume()
        if(listOfCartItem.size>0){
            floating_proced_lay_dish.visibility = View.VISIBLE
            var layout:RelativeLayout.LayoutParams=RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT)
            layout.bottomMargin=70
            nestedScrollView2.layoutParams=layout
        }else{
            floating_proced_lay_dish.visibility= View.GONE
            var layout:RelativeLayout.LayoutParams=RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT)
            layout.bottomMargin=0
            nestedScrollView2.layoutParams=layout
        }
    }

    fun loadAllDishes(view: View){
        startActivity(Intent(this, HomeActivity::class.java))
    }



    private fun setControls(dishId: Int, homeFeed: HomeFeed?){
        val dishInfo: HomeFeed.Dish = homeFeed!!.dishes.filter { d-> d.dish_id == dishId}.single()
        val chefinfo: HomeFeed.Chef = homeFeed.chefs.filter { c-> dishInfo.chef_id == c.chef_id}.single()

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
        btn_buy_detail.setOnClickListener {
            /* val intent = Intent(holder.view.btn_buy_home.context, TimeSlotActivity::class.java)
             holder.view.btn_buy_home.context.startActivity(intent)*/
            btn_lay_detail.visibility=View.VISIBLE
           btn_buy_detail.visibility=View.GONE
        }
        //OnClick BackButton in dishdetails
        fab_detail.setOnClickListener {
            val intent = Intent(fab_detail.context, HomeActivity::class.java)
            fab_detail.context.startActivity(intent)

        }
        tv_minus_detail.setOnClickListener{
            var count=tv_quantity_detail.text.toString().toInt()
            if(count>0) {
                count--
            }
            else{
                btn_lay_detail.visibility=View.GONE
                btn_buy_detail.visibility=View.VISIBLE
            }
            tv_quantity_detail.text=count.toString()
        }
        tv_plus_detail.setOnClickListener{
            var count=tv_quantity_detail.text.toString().toInt()
            if( count<99 ) {
                count++
            }
            tv_quantity_detail.text=count.toString()
        }
    }

    private fun setOngoingRecycleView(buyerHomeFeed: HomeFeed?) {
        recyclerView_dish_detail.layoutManager = LinearLayoutManager(this)
        recyclerView_dish_detail.isNestedScrollingEnabled = false
        //var fourDishes: Array<Dish> = buyerHomeFeed!!.dishes.take(4).toTypedArray()
        // buyerHomeFeed.dishes = fourDishes.toList()
        recyclerView_dish_detail.adapter = DishDetailAdapter(buyerHomeFeed)
    }


    fun OnClickProceed(view: View) {

        var s = "Soumen"
    }


}
