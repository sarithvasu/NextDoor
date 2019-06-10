package com.food.nextdoor.adapter.buyer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import com.food.nextdoor.model.HomeFeed
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.chef_profile_row.view.*
import kotlinx.android.synthetic.main.dish_detail.*
import kotlinx.android.synthetic.main.home_row.view.*

//import com.food.nextdoor.model.ChefProfileFeed

class ChefProfileAdopter (val dishes: List<HomeFeed.Dish>) : RecyclerView.Adapter<ChefProfileViewHolder>() {

    override fun getItemCount(): Int {
        //return homeFeed.dishes.count()
        return dishes.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ChefProfileViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rowObject = layoutInflater.inflate(com.food.nextdoor.R.layout.chef_profile_row, parent, false)
        return ChefProfileViewHolder(rowObject)
    }



    override fun onBindViewHolder(holder: ChefProfileViewHolder, position: Int) {
        // Bind Dish Image
        Picasso.with(holder.view.context).load(dishes.get(position).dish_image_url).into(holder.view.img_signature_dish)
        holder.view.tv_signature_dish_name.text = dishes.get(position).dish_name
        holder.view.tv_signature_dish_rating.setText(dishes.get(position).dish_rating.toString())
        holder.view.tv_signature_dish_rating_bar.rating = dishes.get(position).dish_rating.toFloat()
        }

}


class ChefProfileViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

}