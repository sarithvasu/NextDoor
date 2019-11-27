package com.food.nextdoor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class HomeFeed(val chefs: List<Chef>, var dishes: List<Dish>, val custommeals: CustomMeal?) : Parcelable {

    @Parcelize
    class Chef(
        val ChefId: Int,
        val SellerUserId: Int,
        val SellerPayeeId: Int,
        val FullName: String,
        val FlatNumber: String,
        val Gender: String,
        val VegCount: String,
        val NonVegCount: String,
        val ActiveDishCount: String,
        val ChefAverageRating: Float,
        val ChefTotalDishReview: Int,
        val ChefFollower: Int,
        val AboutChef: String,
        val IsSpecializedInVeg: Boolean,
        val IsSpecializedInNonVeg: Boolean,
        val IsSpecializedInBoth: Boolean,
        val ProfileImageUrl: String,
        val testimonials: List<Testimonial>,
        val signatureDishes: List<SignatureDish>
    ) : Parcelable


    @Parcelize
    class Testimonial(
        val DishName: String, val ReviewerName: String, val ReviewerFlatNumber: String,
        val ReviewNote: String
    ) : Parcelable


    @Parcelize
    class SignatureDish(
        val ChefId: Int, val DishId: String, val DishImageUrl: String, val DishName: String,
        val DishAverageRating: Int, val DishType: String, val UnitPrice: Int, val IsActiveNow: Boolean ) : Parcelable

    @Parcelize
    class Dish(

        val DishId: Int,
        val DishName: String,
        val DishType: String,
        val MealType: String,
        val DishImageUrl: String,
        val DishDescription: String,
        val UnitPrice: Int = -1,
        val ServingsPerPlate: Int,
        val dish_available_date: String,
        val DishAvailableStartTime: String,
        val DishAvailableEndTime: String,
        val TimeSlotInterval: Int,
        val PackingOptions: Int,
        val PackageCharge: Int,
        val DeliveryOptions: Int,
        val DeliveryCharge: Int,
        val ChefId: Int,
        val DishSold: Int,
        val DishAverageRating: Float,
        val DishTotalReview: Int,
        val AvailableQuantity: Int
    ) : Parcelable


    @Parcelize
    class CustomMeal(val menuitems: List<MenuItem>?, val submenuitems: List<SubMenuItem>?) : Parcelable


    @Parcelize
    class MenuItem(val menu_id: Int, val menu_name: String) : Parcelable

    @Parcelize
    class SubMenuItem(val sub_menu_id: Int, val sub_menu_name: String, val sub_menu_unit_price: Int, val menu_id: Int) :
        Parcelable
}