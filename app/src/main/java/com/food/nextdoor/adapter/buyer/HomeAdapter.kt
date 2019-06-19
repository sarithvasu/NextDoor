package com.food.nextdoor.adapter.buyer

import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.food.nextdoor.R
import com.food.nextdoor.activity.buyer.ChefProfileActivity
import com.food.nextdoor.activity.buyer.DishDetailActivity

import com.food.nextdoor.activity.buyer.TimeSlotActivity
import com.food.nextdoor.model.*
import com.google.gson.GsonBuilder

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_row.view.*
import system.CartItem
import system.ShoppingCart
import system.Utility




class HomeAdapter(val homeFeed: HomeFeed?) : RecyclerView.Adapter<HomeBuyerViewHolder>() {

    private lateinit var listOfCartItem :ArrayList<CartItem>
    override fun getItemCount(): Int {
        return homeFeed!!.dishes.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeBuyerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        listOfCartItem = ShoppingCart.getCartItems()

        val cellForRow = layoutInflater.inflate(R.layout.home_row, parent, false)
        return HomeBuyerViewHolder(cellForRow,homeFeed)
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: HomeBuyerViewHolder, position: Int) {

        val chefinfo: HomeFeed.Chef =homeFeed!!.chefs.filter { s-> homeFeed.dishes.get(position).chef_id==s.chef_id}.single()
        val dishInfo: HomeFeed.Dish = homeFeed!!.dishes.get(position)

        // Bind Dish Image
        Picasso.with(holder.view.context).load(dishInfo.dish_image_url).into(holder.view.img_dish_image_home)
        // set Dish Symbol
        val dishSymbol =   Utility.setDishSymbol(dishInfo.dish_type,holder.view.img_dish_symbol_home.context )
        holder.view.img_dish_symbol_home.setImageDrawable(dishSymbol)

        holder.view.tv_dish_name_home.text = dishInfo.dish_name
        holder.view.tv_dish_price_home.text =  " Rs. " + dishInfo.unit_price.toString()
        holder.view.tv_serving_home.text = (dishInfo.servings_per_plate.toString())
        holder.view.tv_chef_name_with_flat_number_home.text = chefinfo.chef_name + " | " + chefinfo.chef_flat_number
        holder.view.tv_dish_available_time_home.text = (dishInfo.dish_available_start_time + " | " + dishInfo.dish_available_end_time)
        //Bind Chef Image
        Picasso.with(holder.view.context).load(chefinfo.chef_profile_image_url).into(holder.view.img_chef_profile_home)
        holder.view.tv_dish_reviews_home.text = dishInfo.dish_rating.toString()
        holder.view.tv_dish_sold_home.text = (dishInfo.dishes_sold).toString()
        holder.view.tv_dish_available_home.text = (dishInfo.available_dishes.toString())
         // Seting the tag for dish Id for detail screen
        holder.view.setTag(dishInfo.dish_id)



        // Set ClickListener for dish Image
        holder.view.img_dish_image_home.setOnClickListener{
            val intent = Intent(holder.view.img_dish_image_home.context, DishDetailActivity:: class.java)
            intent.putExtra(Utility.DISH_ID_KEY, holder.view.getTag() as Int)
            holder.view.img_dish_image_home.context.startActivity(intent)
        }


        // Set ClickListener for chef profile Image
        holder.view.img_chef_profile_home.setOnClickListener{
            val intent = Intent(holder.view.img_chef_profile_home.context, ChefProfileActivity:: class.java)
            intent.putExtra(Utility.CHEF_ID_KEY,chefinfo.chef_id as Int)
            holder.view.img_chef_profile_home.context.startActivity(intent)
        }
        if(listOfCartItem.any { x -> x.dishItem.dishId == dishInfo.dish_id }){
            val cartItem: CartItem= listOfCartItem.filter { s-> dishInfo.dish_id==s.dishItem.dishId}.single()
            if(cartItem.dishItem.quantity>0){
                holder.view.btn_lay.visibility=View.VISIBLE
                holder.view.btn_buy_home.visibility=View.GONE
                holder.view.tv_qutity.text=cartItem.dishItem.quantity.toString()

            }
        }

        // Set ClickListener for buy Button
        holder.view.btn_buy_home.setOnClickListener {
           /* val intent = Intent(holder.view.btn_buy_home.context, TimeSlotActivity::class.java)
            holder.view.btn_buy_home.context.startActivity(intent)*/

            // AddItem(dishInfo)
            val intent = Intent(holder.view.btn_buy_home.context, TimeSlotActivity:: class.java)
            intent.putExtra(Utility.DISH_ID_KEY,dishInfo.dish_id as Int)
            holder.view.btn_buy_home.context.startActivity(intent)




          /*  holder.view.btn_lay.visibility=View.VISIBLE
            holder.view.btn_buy_home.visibility=View.GONE*/
        }

        // Set ClickListener for + Button
        holder.view.tv_plus.setOnClickListener{
            var count=holder.view.tv_qutity.text.toString().toInt()

            if(count<99) {
                count++
            }

            AddItem(dishInfo)
            holder.view.tv_qutity.text=count.toString()
        }

        // Set ClickListener for - Button
        holder.view.tv_minus.setOnClickListener{
            var count=holder.view.tv_qutity.text.toString().toInt()
            if(count>0) {
                count--
            }
            else{
                holder.view.btn_lay.visibility=View.GONE
                holder.view.btn_buy_home.visibility=View.VISIBLE
            }

           RemoveItem(dishInfo)
            holder.view.tv_qutity.text=count.toString()
        }
    }


    private fun AddItem(dishInfo: HomeFeed.Dish) {
        var dishItem: DishItem = DishItem()
        dishItem.dishId = dishInfo.dish_id
        dishItem.deliveryStartTime = dishInfo.dish_available_start_time
        dishItem.deliveryEndTime = dishInfo.dish_available_end_time
        dishItem.packingTypeId = dishInfo.packing_type_id
        dishItem.deliveryTypeId = dishInfo.delivery_type_id
        dishItem.quantity = 1

        val thiCartItem = CartItem(dishItem)
        ShoppingCart.addToCart(thiCartItem)

        // Read back for Checking
        val listOfCartItem = ShoppingCart.getCartItems()



//        val gson = GsonBuilder().serializeNulls().create()
//        val thisorder =  gson.toJson(listOfCartItem.toList())
    }

    private fun RemoveItem(dishInfo: HomeFeed.Dish) {
        var dishItem: DishItem = DishItem()
        dishItem.dishId = dishInfo.dish_id
        dishItem.deliveryStartTime = dishInfo.dish_available_start_time
        dishItem.deliveryEndTime = dishInfo.dish_available_end_time
        dishItem.packingTypeId = dishInfo.packing_type_id
        dishItem.deliveryTypeId = dishInfo.delivery_type_id
        dishItem.quantity = 1

        val thiDishItem = CartItem(dishItem)
        ShoppingCart.removeFromCart(thiDishItem)

        // Read back for Checking
        val cart = ShoppingCart.getCartItems()
    }



}






class HomeBuyerViewHolder(val view: View, val homeFeed: HomeFeed?) : RecyclerView.ViewHolder(view) {

}






