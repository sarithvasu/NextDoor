package com.food.nextdoor.adapter.buyer

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.food.nextdoor.R
import com.food.nextdoor.activity.buyer.ChefProfileActivity
import com.food.nextdoor.activity.buyer.DishDetailActivity
import com.food.nextdoor.model.HomeFeed
import com.food.nextdoor.model.Chef
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_row.view.*
import system.Utility

class HomeAdapter(val homeFeed: HomeFeed?) : RecyclerView.Adapter<HomeBuyerViewHolder>() {
    override fun getItemCount(): Int {
        return homeFeed!!.dishes.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeBuyerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.home_row, parent, false)
        return HomeBuyerViewHolder(cellForRow,homeFeed)
    }

    override fun onBindViewHolder(holder: HomeBuyerViewHolder, position: Int) {

        // Bind Dish Image
        Picasso.with(holder.view.context).load(homeFeed!!.dishes.get(position).dish_image_url).into(holder.view.img_dish_image_home)
        // set Dish Symbol
        val dishSymbol =   Utility.setDishSymbol(homeFeed!!.dishes.get(position).dish_type,holder.view.img_dish_symbol_home.context )
        holder.view.img_dish_symbol_home.setImageDrawable(dishSymbol)

        holder.view.tv_dish_name_home.text = homeFeed.dishes.get(position).dish_name
        holder.view.tv_dish_price_home.text = homeFeed.dishes.get(position).unit_price.toString()
        holder.view.tv_serving_home.text = (homeFeed.dishes.get(position).servings_per_plate.toString())
        //Soumen Bind Chef Name and Flat Number
        val chefinfo: Chef=homeFeed.chefs.filter { s-> homeFeed.dishes.get(position).chef_id==s.chef_id}.single()
        holder.view.tv_chef_name_with_flat_number_home.text = chefinfo.chef_name + " | " + chefinfo.chef_flat_number

        holder.view.tv_dish_available_time_home.text = (homeFeed.dishes.get(position).dish_available_start_time + " | " + homeFeed.dishes.get(position).dish_available_end_time)
        //Bind Chef Image
        Picasso.with(holder.view.context).load(chefinfo.chef_profile_image_url).into(holder.view.img_chef_profile_home)

        holder.view.tv_dish_reviews_home.text = (homeFeed.dishes.get(position).dish_rating.toString())
        holder.view.tv_dish_sold_home.text = (homeFeed.dishes.get(position).dishes_sold).toString()
        holder.view.tv_dish_available_home.text = (homeFeed.dishes.get(position).available_dishes.toString())
         // Seting the tag for dish Id for detail screen
        holder.view.setTag( homeFeed.dishes.get(position).dish_id)

        // Set ClickListener
        holder.view.img_chef_profile_home.setOnClickListener{
            val intent = Intent(holder.view.img_chef_profile_home.context, ChefProfileActivity:: class.java)
            intent.putExtra(Utility.CHEF_ID_KEY,chefinfo.chef_id as Int)
            holder.view.img_chef_profile_home.context.startActivity(intent)


        }
    }
}



class HomeBuyerViewHolder(val view: View, val homeFeed: HomeFeed?) : RecyclerView.ViewHolder(view) {
  init {
            view.setOnClickListener {
            val intent = Intent(view.context, DishDetailActivity::class.java)
                intent.putExtra(Utility.DISH_ID_KEY,view.getTag() as Int)
            view.context.startActivity(intent)
        }
    }
}






