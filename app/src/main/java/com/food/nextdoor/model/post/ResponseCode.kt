package com.food.nextdoor.model.post

data class ResponseCode(
    val HeaderMessageUri: String,
    val Id: Int,
    val Message: String,
    val StatusCode: Int
)