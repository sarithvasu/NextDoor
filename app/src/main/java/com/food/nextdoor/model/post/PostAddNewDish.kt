package com.food.nextdoor.model.post

data class PostAddNewDish(
    val DishId:Int,
    val CategoryId: Int,
    val ChefId: Int,
    val DishDescription: String,
    val DishImageUrl: String,
    val DishName: String,
    val DishType: String,
    val EarningAfterServiceCharges: Int,
    val MealType: String,
    val IsSignatureDish: Boolean,
    val ServingsPerPlate: Int,
    val SubCategoryId: Int,
    val UnitPrice: Int,
    val UserId: Int
)