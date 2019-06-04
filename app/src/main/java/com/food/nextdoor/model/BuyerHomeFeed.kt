package com.food.nextdoor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class BuyerHomeFeed(val chefs: List<Chef>, var dishes: List<Dish>, val custommeals: CustomMeal) : Parcelable
@Parcelize
class Chef(val chef_id: Int, val chef_name: String, val chef_rating: Int, val chef_flat_number: String, val chef_profile_image_url: String) : Parcelable
@Parcelize
class Dish(val dish_id: Int, val dish_name: String, val dish_type:  String , val dish_image_url :  String, val dish_short_description:  String, val dish_long_description:  String, val unit_price:  Int, val  servings_per_plate: Int,  val dish_available_time: String, val time_slot_interval:  String,  val packing_options:  String, val delivery_options:  String, val chef_id:  Int,  val dishes_sold:   Int, val dish_rating:  Int, val dish_ingredients:  String,  val available_dishes:  Int) : Parcelable
@Parcelize
class CustomMeal (val menuitems: List<MenuItem>, val submenuitems: List<SubMenuItem>) : Parcelable
@Parcelize
class MenuItem(val menu_id:  Int,  val menu_name: String) : Parcelable
@Parcelize
class SubMenuItem(val sub_menu_id: Int,  val sub_menu_name: String,  val sub_menu_unit_price: Int, val  menu_id:  Int) : Parcelable

