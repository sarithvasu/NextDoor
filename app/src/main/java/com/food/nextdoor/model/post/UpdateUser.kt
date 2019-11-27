package com.food.nextdoor.model.post

data class UpdateUser(
    val FullName: String,
    val ProfileImageUrl: String?,
    val UserId: Int,
    val FlatNumber: String?,
    val Email : String?,
    val Gender : String
)