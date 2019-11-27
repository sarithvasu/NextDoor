package com.food.nextdoor.adapter.buyer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.food.nextdoor.R
import com.food.nextdoor.model.HomeFeed
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.chef_dish_library_row.view.*


class ChefDishLibraryAdapter( val context: Context, val dishes : List<HomeFeed.Dish>): androidx.recyclerview.widget.RecyclerView.Adapter<DishLibraryViewHolder>() {

    override fun getItemCount(): Int {
        return dishes.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): DishLibraryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rowObject = layoutInflater.inflate(R.layout.chef_dish_library_row, parent, false)
        return DishLibraryViewHolder(rowObject)
    }

    override fun onBindViewHolder(holder: DishLibraryViewHolder, position: Int) {
       // holder.itemView.img_dish_libary.setImageResource(mData.get(position).img)

        // Bind Dish Image
        Picasso.with(holder.itemView.context).load(dishes.get(position).DishImageUrl).into(holder.itemView.img_dish_libary)


    }
}


class DishLibraryViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

}


