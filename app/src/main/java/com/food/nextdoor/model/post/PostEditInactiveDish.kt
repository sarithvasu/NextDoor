package com.food.nextdoor.model.post

data class PostEditInactiveDish(
    val DeliveryCharge: Int,
    val DeliveryTypeId: Int,
    val DishAvailableEndTime: String,
    val DishAvailableStartTime: String,
    val DishDescription: String,
    val DishId: Int,
    val DishImageUrl: String,
    val DishName: String,
    val DishType: String,
    val EarningAfterServiceCharge: Int,
    val MealType: String,
    val PackingCharge: Int,
    val PackingTypeId: Int,
    val PlatformServiceChargePercentage: Int,
    val QuantityPreparing: Int,
    val ServingsPerPlate: Int,
    val TimeSlotInterval: Int,
    val UnitPrice: Int
)