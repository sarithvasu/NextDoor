package com.food.nextdoor.model

data class WriteReviewPost(
    var BuyerId: Int?=0,
    var ChefId: Int?=0,
    var DishId: Int?=0,
    var Ratings: Int?=0,
    var ReviewNote: String?="",
    var TagId: Int?=0
)