package com.food.nextdoor.adapter.buyer

import android.graphics.Color
import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.food.nextdoor.R
import com.food.nextdoor.activity.buyer.WriteReviewActivity
import com.food.nextdoor.listeners.OnItemClickListener
import com.food.nextdoor.model.ReviewTag
import kotlinx.android.synthetic.main.review_tag_layout.view.*

class ReviewTagAdapter(val tagList: ArrayList<ReviewTag>) : androidx.recyclerview.widget.RecyclerView.Adapter<ReviewTagViewHolder>() {

    open var row_index:Int=-1

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ReviewTagViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.review_tag_layout, parent, false)
        return ReviewTagViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return tagList.size
    }

    override fun onBindViewHolder(holder: ReviewTagViewHolder, position: Int) {
        holder.view.tv_review_tag.text = tagList.get(position).tagName


        holder.view.setOnClickListener {

            row_index =position
            notifyDataSetChanged()
        }
        if(row_index==position){
            //holder.tv_time_slot.setBackgroundColor(Color.BLUE)
            holder.view.tv_review_tag.setTextColor(Color.WHITE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.view.tv_review_tag.setBackgroundColor(holder.view.tv_review_tag.context.getColor(R.color.SkyBlue))
            }
            else{
                holder.view.tv_review_tag.setBackgroundColor(holder.view.tv_review_tag.context.resources.getColor(R.color.SkyBlue))
            }
        }
        else{
            //holder.tv_time_slot.setBackgroundColor(Color.parseColor("#ffffff"))
            holder.view.tv_review_tag.setBackgroundColor(Color.WHITE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.view.tv_review_tag.setTextColor(holder.view.tv_review_tag.context.getColor(R.color.SkyBlue))
            }
            else{
                holder.view.tv_review_tag.setTextColor(holder.view.tv_review_tag.context.resources.getColor(R.color.SkyBlue))
            }
        }
    }

    private fun postReview() {
        //var review: Review = Review(1)
    }
}

class ReviewTagViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
}