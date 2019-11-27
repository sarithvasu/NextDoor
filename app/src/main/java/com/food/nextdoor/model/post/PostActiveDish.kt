package com.food.nextdoor.model.post

data class PostActiveDish(
    val DeliveryCharges: Int,
    val DeliveryTypeId: Int,
    val DishAvailableEndTime: String,
    val DishAvailableStartTime: String,
    val DishId: Int,
    val PackingCharges: Int,
    val PackingTypeId: Int,
    val QuantityPreparing: Int,
    val TimeSlotInterval: Int
)