package com.food.nextdoor.adapter.buyer

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.food.nextdoor.R
import com.food.nextdoor.model.HomeFeed

import kotlinx.android.synthetic.main.testimonial_row.view.*
import kotlin.random.Random

class TestimonialAdapter(val items : ArrayList<HomeFeed.Testimonial>, val context: Context): RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.testimonial_row, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size//To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_dishname.text=items[position].dish_name
        holder.tv_testimonial.text=items[position].testimonial_note
        holder.tv_user_testimonial.text=items[position].reviewer_name+"\n"+"A2-503"
        val currentStrokeColor = Color.argb(255, Random.nextInt(200), Random.nextInt(200), Random.nextInt(200))
        val drawable1=holder.ll_item.background
        val drawableNew1: Drawable =setTint(drawable1, currentStrokeColor)
        holder.ll_item.setBackground(drawableNew1)
        holder.tv_dishname.setTextColor(currentStrokeColor)
        holder.tv_testimonial.setTextColor(currentStrokeColor)
    }
    fun setTint(d: Drawable, color: Int): Drawable {
        val wrappedDrawable = DrawableCompat.wrap(d)
        DrawableCompat.setTint(wrappedDrawable, color)
        return wrappedDrawable
    }
}
class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tv_dishname = view.tv_dishname
    val tv_testimonial = view.tv_testimonial
    val tv_user_testimonial=view.tv_user_testimonial
    val ll_item=view.ll_item
}