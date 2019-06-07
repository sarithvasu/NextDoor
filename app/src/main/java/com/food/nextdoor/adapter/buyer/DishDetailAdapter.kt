package com.food.nextdoor.adapter.buyer
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.food.nextdoor.model.HomeFeed
import com.squareup.picasso.Picasso
import com.food.nextdoor.model.Chef
import kotlinx.android.synthetic.main.dish_detail_row.view.*
import system.Utility


class DishDetailAdapter(val homeFeed: HomeFeed?) : RecyclerView.Adapter<DishDetailViewHolder>() {

    override fun getItemCount(): Int {
        return homeFeed!!.dishes.count()
    }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): DishDetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val dishRowObject = layoutInflater.inflate(com.food.nextdoor.R.layout.dish_detail_row, parent, false)
        return DishDetailViewHolder(dishRowObject)
    }


    override fun onBindViewHolder(holder: DishDetailViewHolder, position: Int) {

//        holder.view.tv_available.text = (homeFeed.dishes.get(position).available_dishes).toString()
//        holder.view.tv_sold.text = (homeFeed.dishes.get(position).dishes_sold).toString()
//        // Bind Dish Image
//        Picasso.with(holder.view.context).load(homeFeed.dishes.get(position).dish_image_url).into(holder.view.img_dish)


        //holder.view.tv_rating.text = (homeFeed.dishes.get(position).dish_rating).toString()



        // set Dish Symbol
        val dishSymbol =   Utility.setDishSymbol(homeFeed!!.dishes.get(position).dish_type,holder.view.context)
        holder.view.img_dish_symbol_detail_rv.setImageDrawable(dishSymbol)
        //holder.view.img_dish_symbol_detail.setColorFilter(R.color.Black)


        holder.view.tv_dish_name_detail_main_rv.text = homeFeed!!.dishes.get(position).dish_name
        holder.view.tv_serving_per_person_detail_rv.text = (homeFeed!!.dishes.get(position).servings_per_plate.toString())
        //holder.view.tv_dish_available_time_detail.text = (homeFeed.dishes.get(position).dish_available_time)
        holder.view.tv_dish_price_detail.text = (homeFeed.dishes.get(position).unit_price.toString())

        //Soumen Bind Chef Name and Flat Number
        val chefinfo: Chef =homeFeed.chefs.filter { s-> homeFeed.dishes.get(position).chef_id==s.chef_id}.single()
        holder.view.tv_chef_name_with_flat_number_detail_rv.text = chefinfo.chef_name + " | " + chefinfo.chef_flat_number

        // Bind Dish Image
        Picasso.with(holder.view.context).load(homeFeed.dishes.get(position).dish_image_url).into(holder.view.img_dish_image_detail_rv)

    }



}


class DishDetailViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

}