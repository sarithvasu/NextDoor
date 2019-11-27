package com.food.nextdoor.model

data class ReadReviewNetwork(
    val FlatNumber: String,
    val FullName: String,
    val Ratings: Int,
    val ReviewNote: String,
    val ReviewedDate: String,
    val TagName: String
)