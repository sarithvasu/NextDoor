package com.food.nextdoor.model.post

data class NewDish(
    val CategoryId: Int,
    val ChefId: Int,
    val DateInsertion: String,
    val DateRevision: String,
    val DishDescription: String,
    val DishImageUrl: String,
    val DishName: String,
    val DishRating: Int,
    val DishReview: Int,
    val DishSold: Int,
    val DishType: String,
    val EarningAfterServiceCharges: Int,
    val IsSignatureDish: Boolean,
    val MealType: String,
    val PlatformServiceChargePercentage: Int,
    val ServingsPerPlate: Int,
    val SubCategoryId: Int,
    val UnitPrice: Int
)