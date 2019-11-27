package com.food.nextdoor.model.post

data class PostActiveDishForFirst(
    val DeliveryCharge: Int,
    val UserId:Int,
    val DeliveryOptions: Int,
    val DishAvailableEndTime: String,
    val DishAvailableStartTime: String,
    val DishId: Int,
    val PackingCharge: Int,
    val PackingOptions: Int,
    val ChefId: Int,
    val QuantityPreparing: Int,
    val TimeSlotInterval: Int
)