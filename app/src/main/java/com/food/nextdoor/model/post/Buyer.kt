package com.food.nextdoor.model.post

 data class Buyer(
    val ApartmentId: Int,
    val Email: String,
    val FlatNumber: String,
    val FullName: String,
    val Gender: String,
    val IsEmailVerified: Boolean,
    val IsMobileNumberVerified: Boolean,
    val MobileNumber: String,
    val ProfileImageUrl: String,
    val UserTypeId: Int
)