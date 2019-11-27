package com.food.nextdoor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyOrderFeed ( val myOrders : List<MyOrder>) : Parcelable

@Parcelize
data class MyOrder (
    val dish_id : Int,
    val dish_name : String,
    val dish_type : String,
    val unit_price : Int,
    val quantity : Int,
    val chef_name : String,
    val chef_flat_number : String,
    val order_id : String,
    val order_date : String,
    val status : String,
    val dish_rating : Int,
    val review_id : Int,
    val review : Review
) : Parcelable


@Parcelize
data class Review (
    val buyer_id : Int,
    val dish_id : Int,
    val tag_id : Int,
    val review_note : String,
    val review_written_date : String,
    val tag_name : String,
    val dish_rating : Int,
    val buyer_name : String,
    val buyer_flat_number : String,
    val like : Int,
    val dislike : Int
) : Parcelable





