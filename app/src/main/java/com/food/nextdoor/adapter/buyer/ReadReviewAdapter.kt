package com.food.nextdoor.adapter.buyer

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.food.nextdoor.R
import com.food.nextdoor.model.ReadReviewFeed
import com.food.nextdoor.model.Review
import kotlinx.android.synthetic.main.read_review_row.view.*
import androidx.core.view.MenuItemCompat.getContentDescription
import com.food.nextdoor.model.ReadReviewNetwork


//import android.R





class ReadReviewAdapter(val cotext: Context, val readReviewFeed: ArrayList<ReadReviewNetwork>) : androidx.recyclerview.widget.RecyclerView.Adapter<ReadReviewFeedViewHolder>(){

    override fun getItemCount(): Int {
        return readReviewFeed.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ReadReviewFeedViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rowObject = layoutInflater.inflate(com.food.nextdoor.R.layout.read_review_row, parent, false)
        return ReadReviewFeedViewHolder(rowObject)
    }

    override fun onBindViewHolder(holder: ReadReviewFeedViewHolder, position: Int) {
        val review = readReviewFeed[position]
        holder.setData(review,position)



        holder.view.img_like_review_rv.setOnClickListener {
            var likeNumber =  holder.view.tv_dish_likes_review_row.text.toString().toInt()
            holder.view.tv_dish_likes_review_row.text =  (likeNumber + 1).toString()
        }


        holder.view.img_dislike_review_rv.setOnClickListener {
            var dislikeNumber =  holder.view.tv_dish_dislikes_review_row.text.toString().toInt()
            holder.view.tv_dish_dislikes_review_row.text =  (dislikeNumber - 1).toString()
        }



    }

    private fun flipLikeDislike(holder: ReadReviewFeedViewHolder) {
        if (holder.view.img_like_review_rv.id == R.id.img_like_review_rv) {
            holder.view.img_like_review_rv.setImageResource(R.drawable.dislike)
        } else if (holder.view.img_dislike_review_rv.id == R.id.img_dislike_review_rv) {
            holder.view.img_dislike_review_rv.setImageResource(R.drawable.like)
        }
    }


}


class ReadReviewFeedViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    fun setData(review: ReadReviewNetwork, position: Int) {
        // set Like and Dislike Symbol
        var drawableLike: Drawable? = ContextCompat.getDrawable(view.img_like_review_rv.context, R.drawable.like)
        view.img_like_review_rv.setImageDrawable(drawableLike)
        var drawableDisLike: Drawable? = ContextCompat.getDrawable(view.img_dislike_review_rv.context, R.drawable.dislike)
        view.img_dislike_review_rv.setImageDrawable(drawableDisLike)

        view.tv_review_tag_name_row.text = review.TagName
        view.ratingbar_review_rv.rating = review.Ratings.toFloat()
        view.tv_buyer_name_review_row.text = review.FullName.toString()
        view.tv_chef_address_with_flat_number_review_row.text = review.FlatNumber.toString()
        view.tv_date_review_row.text = review.ReviewedDate.toString()
        view.tv_review_discription_review_row.text = review.ReviewNote.toString()

    }

}

