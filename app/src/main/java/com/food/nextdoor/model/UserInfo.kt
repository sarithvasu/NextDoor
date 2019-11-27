package com.food.nextdoor.model

data class UserInfo(
    val Apartment: UserApartment,
    val User: User
)

data class UserApartment(
    val ActiveChefCount: Int,
    val ActiveUserCount: Int,
    val ApartmentId: Int,
    val ApartmentName: String,
    val CityId: Int,
    val PinCode: String
)

data class User(
    val ApartmentId: Int,
    val Email: String,
    val FlatNumber: String,
    val FullName: String,
    val Gender: String,
    val IsEmailVerified: Boolean,
    val IsMobileNumberVerified: Boolean,
    val MobileNumber: String,
    val ProfileImageUrl: String,
    val UserId: Int,
    val UserTypeId: Int
)