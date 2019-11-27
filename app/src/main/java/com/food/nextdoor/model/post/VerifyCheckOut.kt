package com.food.nextdoor.model.post

data class VerifyCheckOut(
    val DishAvailableEndTime: String?,
    val DishId: Int,
    val Quantity: Int
)