package com.food.nextdoor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class HomeFeed(val chefs: List<Chef>, var dishes: List<Dish>, val custommeals: CustomMeal) : Parcelable {

    @Parcelize
    class Chef(
        val chef_id: Int,
        val seller_user_id: Int,
        val seller_payee_id: Int,
        val chef_name: String,
        val chef_flat_number: String,
        val chef_gender: String,
        val chef_rating: Int,
        val chef_review: Int,
        val chef_follower: Int,
        val about_chef: String,
        val specialized_in_veg: Boolean,
        val is_specialized_in_non_veg: Boolean,
        val chef_profile_image_url: String,
        val testimonials: List<Testimonial>
    ) : Parcelable {

//        private constructor(): this (0,0,0,"","","",0,0,0,
//            "",true,true,"",null)
    }


    @Parcelize
    class Testimonial(
        val dish_name: String, val reviewer_name: String, val reviewer_flat_number: String,
        val testimonial_note: String
    ) : Parcelable


    @Parcelize
    class Dish(
        val dish_id: Int,
        val dish_name: String,
        val dish_type: String,
        val dish_image_url: String,
        val dish_description: String,
        val unit_price: Int,
        val servings_per_plate: Int,
        val dish_available_start_time: String,
        val dish_available_end_time: String,
        val time_slot_interval: String,
        val packing_type_id: Int,
        val packing_description: String,
        val packing_charges: Int,
        val delivery_type_id: Int,
        val delivery_description: String,
        val delivery_charges: Int,
        val chef_id: Int,
        val dishes_sold: Int,
        val dish_rating: Int,
        val available_dishes: Int
    ) : Parcelable


    @Parcelize
    class CustomMeal(val menuitems: List<MenuItem>, val submenuitems: List<SubMenuItem>) : Parcelable


    @Parcelize
    class MenuItem(val menu_id: Int, val menu_name: String) : Parcelable

    @Parcelize
    class SubMenuItem(val sub_menu_id: Int, val sub_menu_name: String, val sub_menu_unit_price: Int, val menu_id: Int) :
        Parcelable
}