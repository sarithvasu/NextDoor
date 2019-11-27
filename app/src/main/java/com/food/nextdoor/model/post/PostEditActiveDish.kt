package com.food.nextdoor.model.post

data class PostEditActiveDish(
    val DishAvailableEndTime: String,
    val DishAvailableStartTime: String,
    val DishId: Int,
    val QuantityPreparing: Int
)