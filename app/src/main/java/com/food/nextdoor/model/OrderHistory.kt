package com.food.nextdoor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderHistory(

    val Dishes: List<Dishe>,
    val OrderSummary: List<OrderSummary>,
    val Reviews: List<Reviews>
)  : Parcelable
@Parcelize
data class Dishe(
    val DeliverEndTime: String,
    val DeliverStartTime: String,
    val DishId: Int,
    val DishName: String,
    val GroupId: Int,
    val Quantity: Int,
    val TotalDeliveryCharges: Int,
    val TotalPackingCharges: Int,
    val UnitPrice: Int
) : Parcelable
@Parcelize
data class OrderSummary(
    val ChefFlatNumber: String,
    val ChefId: Int,
    val ChefName: String,
    val DishRating: Int,
    val GroupId: Int,
    val ItemCount: Int,
    val OrderDate: String,
    val OrderStatus: String?,
    val OrderTotal: Int,
    val PaymentMode: String
): Parcelable
@Parcelize
data class Reviews(
    val BuyerId: Int,
    val BuyerName: String,
    val DishId: Int,
    val Ratings: Int,
    val ReviewNote: String,
    val ReviewedDate: String,
    val TagId: Int,
    val TagName: String
): Parcelable