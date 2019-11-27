package com.food.nextdoor.model.post

data class ActivateDish(
    val DateInsertion: String,
    val DateRevision: String,
    val DeliveryCharges: Int,
    val DeliveryTypeId: Int,
    val DishAvailableEndTime: String,
    val DishAvailableStartTime: String,
    val DishId: Int,
    val DishPreparingTime: Int,
    val IsActive: Boolean,
    val PackingCharges: Int,
    val PackingTypeId: Int,
    val QuantityPreparing: Int,
    val TimeSlotInterval: Int
)