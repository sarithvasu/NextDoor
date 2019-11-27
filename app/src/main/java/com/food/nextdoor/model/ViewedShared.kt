package com.food.nextdoor.model

data class ViewedShared(
    val DishName: String,
    val DurationInSeconds: Int,
    val FlatNumber: String,
    val FullName: String,
    val MobileNumber: String,
    val ViewedEndTime: String,
    val ViewedStartTime: String,
    val SharedVia: String,
    val UserName: String
)