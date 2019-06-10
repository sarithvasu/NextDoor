package com.food.nextdoor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ChefProfileFeed1( val chef_profile: ChefProfile) : Parcelable

@Parcelize
class ChefProfile(val chef_id: Int, val chef_name: String, val chef_flat_number: String, val about_chef: String, val chef_gender: String, val chef_specialty: String, val chef_review: Int, val chef_rating: Int, val chef_follower: Int, val testimoniallist: List<HomeFeed.Testimonial>, var signaturedishlist: List<SignatureDish1>) : Parcelable

@Parcelize
class Testimonial1(val testimonial_id: Int, val testimonial_note: String,val testimonial_date: String, val dish_name: String,val reviewer_id: Int, val reviewer_name: String) : Parcelable

@Parcelize
class SignatureDish1(val signature_dish_id: Int, val signature_dish_name: String,val signature_short_description: String, val signature_dish_rating: Int,val signature_dish_url: String) : Parcelable
