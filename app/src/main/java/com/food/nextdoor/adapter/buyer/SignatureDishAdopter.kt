package com.food.nextdoor.adapter.buyer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.food.nextdoor.model.ChefProfileFeed

class SignatureDishAdopter (val chefProfileFeed: ChefProfileFeed) : RecyclerView.Adapter<ChefProfileViewHolder>() {

    override fun getItemCount(): Int {
        //return buyerHomeFeed.dishes.count()
        return 3
    }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ChefProfileViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rowObject = layoutInflater.inflate(com.food.nextdoor.R.layout.chef_profile_row, parent, false)
        return ChefProfileViewHolder(rowObject)
    }



    override fun onBindViewHolder(holder: ChefProfileViewHolder, position: Int) {
        // Bind Dish Image
//        Picasso.with(holder.view.context).load(chefProfileFeed.chef_profile.signaturedishlist.get(position).signature_dish_url).into(holder.view.img_signature_dish)
//
//        holder.view.tv_signature_dish_name.text = chefProfileFeed.chef_profile.signaturedishlist.get(position).signature_dish_name
//        holder.view.tv_signature_dish_description.text = chefProfileFeed.chef_profile.signaturedishlist.get(position).signature_short_description
//        holder.view.tv_signature_dish_rating.text = chefProfileFeed.chef_profile.signaturedishlist.get(position).signature_dish_rating.toString()
//        holder.view.tv_signature_dish_rating_bar.rating = chefProfileFeed.chef_profile.signaturedishlist.get(position).signature_dish_rating.toFloat()
    }

}


class ChefProfileViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

}